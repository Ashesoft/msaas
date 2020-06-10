# readme

说明: 这是闲暇时自己玩的一个小demo, 意在学学主流的技术, 故在代码中有菜鸟之类的表现还望大神们指正, 本人将不胜感激;

## 前台(html+css+js)
* 页面以及样式是在网上淘的, 或者自己简单画的;
* js没有使用框架, 而是根据需要写的;

## 后台(springboot)
* 数据库`mysql`, 它的jdbc驱动推荐`com.mysql.cj.jdbc.Driver`, 而`com.mysql.jdbc.Driver`已经被弃用了;
* 持久层: 直接使用的是`spring-boot-starter-jdbc`, 所以放弃了一张数据表对应一个实体类的做法, 改用`EntityBean`代替;
* 连接池: springboot默认的`hikari`;
* 服务层: 略;
* 控制层: 略;

## 全局的一些个处理
* 使用`jasypt-spring-boot-starter`对配置文件中的敏感信息做了加密, 具体[请了解](https://github.com/Ashesoft/iosplay/blob/master/Jasypt%20%E5%9C%A8%20springboot%20%E4%B8%AD%E7%9A%84%E5%88%9D%E6%AD%A5%E4%BD%BF%E7%94%A8.md);
* 使用`java-jwt`对资源访问做了简单的授权跟鉴权;
* 对demo中可能出现的异常做了, 做了全局的异常处理;
* 使用`jaudiotagger`对音频文件做简单的处理后存库;

## 其他
原本设想的demo架构是:
* springboot-controller
* springboot-service
* springboot-resources
* springboot-api
* springboot-authorcenter
* ...
