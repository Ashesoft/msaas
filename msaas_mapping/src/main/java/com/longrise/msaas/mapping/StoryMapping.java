package com.longrise.msaas.mapping;

import com.longrise.msaas.global.domain.EntityBean;
import com.longrise.msaas.global.excutor.JDBCExcutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Repository
public class StoryMapping {

    @Autowired
    private JDBCExcutor jdbcExcutor;

    /**
     * 获取故事目录
     *
     * @return 列表
     */
    public EntityBean[] getBookList() {
        String sql = "select sid, stitlename from storybook";
        return jdbcExcutor.querys(sql);
    }

    /**
     * 获取当前与之相邻的上下两行的三行章回内容
     *
     * @param sid 当前章回的id
     * @return 当前章回加与之相邻的上下两行的三行数据
     */
    public EntityBean getBookContent(Long sid) {
        String sql = "select stitlename, scontent from storybook where sid=:sid";
        EntityBean bean = new EntityBean(1);
        bean.put("sid", sid);
        bean.put("curr_data", jdbcExcutor.query(sql, bean));
        sql = "select sign(sid-:sid) as stype, case " +
                "when sign(sid-:sid)<0 then max(sid) " +
                "when sign(sid-:sid)>0 then min(sid) " +
                "else sid " +
                "end as stitleid " +
                "from storybook group by stype";
        EntityBean[] beans = jdbcExcutor.querys(sql, bean);
        bean.put("prev_sid", "没有了");
        bean.put("next_sid", "没有了");
        Arrays.asList(beans).forEach(b -> {
            String stype = String.valueOf(b.get("stype"));
            if ("-1".equals(stype)) {
                bean.put("prev_sid", b.get("stitleid"));
            } else if ("1".equals(stype)) {
                bean.put("next_sid", b.get("stitleid"));
            }
        });
        return bean;
    }

    /**
     * 批量向数据库新增故事书数据
     *
     * @param beans Map数组
     * @return 返回对应新增所影响的行数集合
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insetBookInfo(EntityBean[] beans) {
        String sql = "insert into storybook (sid, createtime, sbookname, stitlename, scontent) " +
                "values (:sid, :createtime, :sbookname, :stitlename, :scontent)";
        return jdbcExcutor.insertUpdateDeletes(sql, beans);
    }

    /**
     * 常用CURD操作大致使用以下三个方法:
     * 1.execute方法，用于直接执行SQL语句
     * 2.update方法，用户新增修改删除操作 -- 其中有个批量的操作
     * 3.query方法，用于查询方法
     */
}
