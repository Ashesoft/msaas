package com.longrise.msaas.global.utils;


/**
 * Twitter_Snowflake <br>
 * Snowflake 的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识, 由于long基本类型在Java中是带符号的, 最高位是符号位, 正数是0, 负数为1, 因为id一般是正数, 故最高位是0<br>
 * 41位时间戳(毫秒级), 注意, 41位时间戳不是存储当前时间的时间戳, 而是存储时间戳的差值(当前时间戳 - 开始时间戳 = 得到的值),
 * 这里的开始时间戳, 一般是我们的id生成器开始使用的时间, 由我们程序来指定(如下面程序IdWorker类的twepoch属性).
 * 41位的时间戳, 可以使用69年, 年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位, 可以部署1024个节点, 包括5位datacenterId和5位workerId<br>
 * 12位序列, 毫秒内的计算, 12位的计算顺序号支持每个节点每毫秒(同一机器, 同一时间戳)产生4096个ID序号<br>
 * 加起来刚好64位, 为一个Long型.<br>
 * SnowFlake的优点是, 整体上按照时间自增排序, 并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID做区分), 并且效率高效,
 * 经测试, SnowFlake每秒能够产生26万多个ID
 */
public class IdWorker {

    //下面两个每个5位, 加起来就是10位的工作机器id
    private long workerId;    //工作机器id(0~31)
    private long datacenterId;   //数据中心id(0~31)
    //12位的序列号(毫秒内序列(0~4095))
    private long sequence;

    //长度为5位(机器id所占的位数)
    private long workerIdBits = 5L;

    //数据标识id所占的位数
    private long datacenterIdBits = 5L;

    //序列号id长度(序列号在id中占用的位数)
    private long sequenceBits = 12L;

    //序列号最大值(生成序列的掩码, 这里为4095 (0b111111111111=0xfff=4095))
    private long sequenceMask = ~(-1L << sequenceBits);

    //机器id需要左移的位数, 12位
    private long workerIdShift = sequenceBits;

    //数据标识id需要左移位数 12+5=17位
    private long datacenterIdShift = sequenceBits + workerIdBits;

    //时间戳需要左移位数 12+5+5=22位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    //上次时间戳, 初始值为负数
    private long lastTimestamp = -1L;

    public IdWorker(long workerId, long datacenterId, long sequence) {
        // sanity check for workerId
        //支持的最大机器id, 结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
        long maxWorkerId = ~(-1L << workerIdBits);
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                    maxWorkerId));
        }
        //支持的最大数据标识id, 结果是31
        long maxDatacenterId = ~(-1L << datacenterIdBits);
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",
                    maxDatacenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, " +
                        "sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    //下一个ID生成算法
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次id生成的时间戳, 说明系统时钟回退过这个时间, 应当抛出异常
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d " +
                            "milliseconds",
                    lastTimestamp - timestamp));
        }

        //获取当前时间戳如果等于上次时间戳（同一毫秒内）, 则在序列号加一；否则序列号赋值为0, 从0开始。
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒, 获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
            // 时间戳改变, 毫秒内序列重置
        } else {
            sequence = 0;
        }

        //将上次时间戳值刷新 单位是毫秒
        lastTimestamp = timestamp;

        // 返回结果：
        // (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳, 再左移相应位数
        // (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
        // (workerId << workerIdShift) 表示将工作id左移相应位数
        // | 是按位或运算符, 例如：x | y, 只有当x, y都为0的时候结果才为0, 其它情况结果都为1。
        // 因为个部分只有相应位上的值有意义, 其它位上都是0, 所以将各部分的值进行 | 运算就能得到最终拼接好的id
        // 初始时间戳(开始时间戳  2010-11-04 09:42:54.657)
        long twepoch = 1288834974657L;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    /**
     * 阻塞到下一个毫秒, 直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    //---------------测试---------------
    public static void main(String[] args) {
        IdWorker worker = new IdWorker(1, 1, 1);
        for (int i = 0; i < 3000; i++) {
            System.out.println(worker.nextId());
        }
    }

}
