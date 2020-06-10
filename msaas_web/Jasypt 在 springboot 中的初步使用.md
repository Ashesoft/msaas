# Jasypt 在 springboot 中的初步使用
## 一. 引入依赖
```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.2</version>
</dependency>
```

## 二. application.xml文件配置
```properties
//自定义加密器(推荐使用)
jasypt.encryptor.bean=codeSheepEncryptorBean
//自定义加密前后缀,默认为(ENC())
jasypt.encryptor.propery.prefix=CodeSheep(
jasypt.encryptor.propery.suffix=)
```
+ 看到这里有人会问了, 上面的配置中没有加密的密钥`jasypt.encryptor.password=xxxx`, 那怎么加密呢? 解决在自定义加密器中定义.

## 三. 加密器实现
```java
@Configuration
public class CodeSheepEncryptorConfig {
    @Bean( name = "codeSheepEncryptorBean" ) // 一注意要跟配置文件的一致
    public StringEncryptor codesheepStringEncryptor() {

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 通过使用jvm环境的方式隐蔽jasypr的加密密钥
        config.setPassword(System.getProperty("jasypt.encryptor.password"));
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }
}
```
+ 注意这里获取password的方式设置jvm环境的方式, 至于环境名称可以自定义, 两边要保持一致.
+ 这样代码中就不会暴露用来加密的密钥了.

## 四. 编写测试类加密要加密的明文
```java
@SpringBootApplication
public class SpringBootConfigEncryptApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext appCtx;

    @Autowired
    private StringEncryptor codeSheepEncryptorBean;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConfigEncryptApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Environment environment = appCtx.getBean(Environment.class);

        // 首先获取配置文件里的原始明文信息
        String uname = environment.getProperty("spring.datasource.username");
        String upwd = environment.getProperty("spring.datasource.password");

        // 加密
        String euname = encrypt( uname );
        String eupwd = encrypt( upwd );

        // 打印加密前后的结果对比
        System.out.println( "==================明文=================" );
        System.out.println( "uname：" + mysqlOriginPswd );
        System.out.println( "upwd：" + redisOriginPswd );
        System.out.println( "==================密文=================" );
        System.out.println( "euname：" + mysqlEncryptedPswd );
        System.out.println( "eupwd：" + redisEncryptedPswd );
    }

    private String encrypt( String originPassord ) {
        String encryptStr = codeSheepEncryptorBean.encrypt( originPassord );
        return encryptStr;
    }

    private String decrypt( String encryptedPassword ) {
        String decryptStr = codeSheepEncryptorBean.decrypt( encryptedPassword );
        return decryptStr;
    }
}
```
运行项目, 打印控制台:
```text
==================明文=================
uname：root
upwd：root
==================密文=================
euname：k8xGp81xIDrTmmShqpkXZg5+ZlH8e4MN8TvMz6I8NvlWoe2SMjmchxHpMeNzXoE6
eupwd：JZKecUE8aqUuOLJnbI4zKyNEARWx3MxPNOdLXz/5IzYBatH/mU3JOdLfds/NCwTr
```
## 五. 修改配置文件, 替换待加密配置项
我们拿到上一步得到的加密结果，将配置文件中的原始明文密码替换成上一步对应的结果即可，就像这样：
```yaml
spring:
    datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/springboot?charset=utf8mb4&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
        username: CodeSheep(k8xGp81xIDrTmmShqpkXZg5+ZlH8e4MN8TvMz6I8NvlWoe2SMjmchxHpMeNzXoE6)
        password: CodeSheep(JZKecUE8aqUuOLJnbI4zKyNEARWx3MxPNOdLXz/5IzYBatH/mU3JOdLfds/NCwTr)
```
## 六. 查看密码解密结果
```java
@SpringBootApplication
public class SpringBootConfigEncryptApplication implements CommandLineRunner{

    @Autowired
    private ApplicationContext appCtx;

    @Autowired
    private StringEncryptor codeSheepEncryptorBean;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConfigEncryptApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Environment environment = appCtx.getBean(Environment.class);

        // 首先获取配置文件里的配置项
        String uname = environment.getProperty("spring.datasource.username");
        String upwd = environment.getProperty("spring.datasource.password");

        // 打印解密后的结果
        System.out.println( "uname：" + uname );
        System.out.println( "upwd：" + upwd );
    }
}
```   
打印结果：
```text
uname：root
upwd：root
```
## 七. 加密密钥不要放在项目中
本人的解决方案是:

直接作为程序启动时的`应用环境变量`来带入
```text
java -Djasypt.encryptor.password=****** -jar xxx.jar
```
另外还有两种方式:
+ 直接作为程序启动时的`命令行参数`来带入
```text
java -jar xxx.jar --jasypt.encryptor.password=******
```
+ 作为`系统环境变量`的方式来带入
比方说，我们提前设置好系统环境变量`JASYPT_ENCRYPTOR_PASSWORD=******`，则直接在Spring Boot的项目配置文件中做如下配置即可：
```yaml
jasypt:
  encryptor:
    password: ${JASYPT_ENCRYPTOR_PASSWORD:}
```