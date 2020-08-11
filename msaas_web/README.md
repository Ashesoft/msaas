# readme

说明: 这是闲暇时自己玩的一个小demo, 意在学主流的技术, 故在代码中有菜鸟之类的表现还望大神们指正, 不胜感激;

## 前台(html+css+js)
* 页面以及样式是在网上淘的, 或者自己简单画的;
* js没有使用框架, 而是根据需要写的;
* 也可以是用`thymeleaf`模板引擎

## 后台(springboot)
* 数据库`mysql`, 它的jdbc驱动推荐`com.mysql.cj.jdbc.Driver`, 而`com.mysql.jdbc.Driver`已经被弃用了;
* 持久层: 直接使用的是`spring-boot-starter-jdbc`, 为了放弃了一张数据表对应一个实体类的做法, 改用`EntityBean`代替;
* 连接池: springboot默认的`hikari`;
* 服务层: 略;
* 控制层: 略;

## 全局的一些个处理
* 使用`jasypt-spring-boot-starter`对配置文件中的敏感信息做了加密, 具体[请了解](https://github.com/Ashesoft/msaas/blob/master/msaas_web/Jasypt%20%E5%9C%A8%20springboot%20%E4%B8%AD%E7%9A%84%E5%88%9D%E6%AD%A5%E4%BD%BF%E7%94%A8.md);
* 使用`java-jwt`对资源访问做了简单的授权跟鉴权;
* 对demo中可能出现的异常做了, 做了全局的异常处理;
* 使用`jaudiotagger`对音频文件做简单的处理后存库;

## 自定义starter
* 首先使用idea工具创建一个的空项目; 如`hell-world`
* 在这个空项目中添加一个starter的module; 如`hello-spring-boot-starter`
    * 这个starter是一个空的maven依赖module
    ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>www.longrise.hello</groupId>
      <artifactId>hello-spring-boot-starter</artifactId>
      <version>1.0-SNAPSHOT</version>
      <dependencies>
          <dependency>
              <groupId>www.longrise.autoconfig.hello</groupId>
              <artifactId>hello-spring-boot-autoconfig</artifactId>
              <version>1.0-SNAPSHOT</version>
          </dependency>
      </dependencies>
  </project>
    ```
    * 这个module中没有任何的类, 只是手动引入了我们待会儿创建的自动装配module
* 定义`hell-world`的自动装配配置module
    * 首先创建一个只包含`spring-boot-starter`或也包含`spring-boot-configuration-processor`依赖的springboot module
    ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.3.2.RELEASE</version>
      </parent>
      <groupId>www.longrise.autoconfig.hello</groupId>
      <artifactId>hello-spring-boot-autoconfig</artifactId>
      <version>1.0-SNAPSHOT</version>
      <properties>
          <java.version>11</java.version>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
      </properties>
      <dependencies>
          <!-- 要想使用自定义starter就必须引入这个依赖 -->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter</artifactId>
          </dependency>
          <!-- 自动配置项提示处理器 -->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-configuration-processor</artifactId>
              <optional>true</optional>
          </dependency>
      </dependencies>
  </project>
    ```
    * 删除跟自动装配没有关联的文件, 只保留如下:
    ```text
  + hello-spring-boot-autoconfigure
    + src
      + main
        + java
          + www.longrise.autoconfig.hello
            - MyHelloAutoConfiguration.java
            - MyHello.java
            - MyHelloProperties.java
        + resources
          + META-INF
            - spring.factories
    - pom.xml
    ```
    * 具体自定义的代码规范请参考[官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-developing-auto-configuration)
* 使用maven插件将编写的两个module安装(install)到仓库
* 安装完成后就可以在springboot项目中引入我们自定义的starter依赖,而这个starter又依赖我们自定义的自动装配, 这时springboot就会自动装配这个starter
中的自动装配了

**ps:** 部分Java代码
* `MyHelloAutoConfiguration.java`自动装配配置类 
```java
package www.longrise.autoconfig.hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 表示这是一个配置类
@Configuration
// 装配条件是否为servlet的web服务
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
// 自动装配时需要绑定的自定义的配置属性, 此配置是可以在配置文件中更改的
@EnableConfigurationProperties(MyHelloProperties.class)
public class MyHelloAutoConfiguration {
    // 因为系统在装配这个配置类时就已经先装配了MyHelloProperties, 所以可以直接从容器中获取
    @Autowired
    private MyHelloProperties myHelloProperties;
    // 创建跟属性配置类绑定的MyHello对象
    @Bean
    public MyHello myHello(){
        return new MyHello(myHelloProperties);
    }
}
```
* `MyHelloProperties.java`自动装配时需要的属性
```java
package www.longrise.autoconfig.hello;
import org.springframework.boot.context.properties.ConfigurationProperties;
// 自动装配时需要在配置文件中自定义的属性, 匹配以 my.hello 开头的属性配置
@ConfigurationProperties("my.hello")
public class MyHelloProperties {
    private String prefix; // 前缀
    private String suffix; // 后缀
    // 其他属性略...
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
```
* `MyHello.java`最终要自动装配的bean
```java
package www.longrise.autoconfig.hello;
// 最终要自动装配的bean
public class MyHello {
    private MyHelloProperties myHelloProperties;
    public MyHello(MyHelloProperties myHelloProperties){
        this.myHelloProperties = myHelloProperties;
    }
    public String sayHello(String name){
        return String.format("%s%s%s 说了句 Hello World!", this.myHelloProperties.getPrefix(), name, this.myHelloProperties.getPrefix());
    }
}
```
* `spring.factories`自动装配的工厂配置文件
```properties
# 自动装配工厂的配置(激活那些配置类为自动装配)
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
www.longrise.autoconfig.hello.MyHelloAutoConfiguration
```


