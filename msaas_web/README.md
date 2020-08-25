# readme

说明: 这是闲暇时自己玩的一个小demo, 意在学主流的技术, 故在代码中有菜鸟之类的表现还望大神们指正, 不胜感激;

## 前台(html+css+js)
* 页面以及样式是在网上淘的, 或者自己简单画的;
* js没有使用框架, 而是根据需要写的;
* 也可以是用`thymeleaf`模板引擎

## 后台(springboot)
* 数据库`mysql`, 它的jdbc驱动推荐`com.mysql.cj.jdbc.Driver`, 而`com.mysql.jdbc.Driver`已经被弃用了;
    * 通过`org.springframework.boot.jdb.DatabaseDriver` 中的 fromJdbcUrl(String url) 方法, 根据数据库 url
     链接地址获取主流数据库的驱动
* 持久层: 直接使用的是`spring-boot-starter-jdbc`, 为了放弃了一张数据表对应一个实体类的做法, 改用`EntityBean`代替;
* 连接池: springboot默认的`hikari`, 重点关注`org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`
* 服务层: 使用了默认的缓存服务;
* 控制层: 略;

## 全局的一些个处理
* 使用`jasypt-spring-boot-starter`对配置文件中的敏感信息做了加密, 具体[请了解](https://github.com/Ashesoft/msaas/blob/master/msaas_web/Jasypt%20%E5%9C%A8%20springboot%20%E4%B8%AD%E7%9A%84%E5%88%9D%E6%AD%A5%E4%BD%BF%E7%94%A8.md);
* 使用`java-jwt`对资源访问做了简单的授权跟鉴权;
* 对demo中可能出现的异常做了, 做了全局的异常处理;
* 使用`jaudiotagger`对音频文件做简单的处理后存库;
* 对 HTTP 请求传入的参数进行自定义封装, 具体请参考`HandlerMethodArgumentResolver`接口极其实现类, 或者如下:
```java
import com.longrise.msaas.global.domain.EntityBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;import java.util.Map;

@Configuration
public class HTTPParamConvert implements WebMvcConfigurer{
    /**
     * 添加请求参数解析器
     */   
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new HandlerMethodArgumentResolver() {
            @Override 
            public boolean supportsParameter(MethodParameter parameter) {
               return EntityBean.class == parameter.getParameterType();
            }
            @Override 
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)throws Exception {
                Map<String,String[]> parameterMap = webRequest.getParameterMap();
                EntityBean bean = new EntityBean(parameterMap.size());
                parameterMap.forEach((k, v)->{
                    bean.put(k, v[0]);
                }); 
                return bean;
            }
        });
    }   
}
```

## 缓存
* 使用`@EnableCaching`注解在主类上以开启缓存支持(注解版)
* springboot 默认支持以下缓存组件, 导入对应的starter就可以直接使用了
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(CacheManager.class)
@ConditionalOnBean(CacheAspectSupport.class)
@ConditionalOnMissingBean(value = CacheManager.class, name = "cacheResolver")
@EnableConfigurationProperties(CacheProperties.class)
@AutoConfigureAfter({ CouchbaseAutoConfiguration.class, HazelcastAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class, RedisAutoConfiguration.class })
@Import({ CacheConfigurationImportSelector.class, CacheManagerEntityManagerFactoryDependsOnPostProcessor.class })
public class CacheAutoConfiguration {
    static class CacheConfigurationImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            CacheType[] types = CacheType.values(); // 这里定了一个缓存类型枚举类
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                // 使用缓存配置工具类, 通过缓存类型枚举类的值, 查找所有的支持的缓存组件, 再根据条件进行自动装配
                imports[i] = CacheConfigurations.getConfigurationClass(types[i]);
            }
            return imports;
        }
    }
}
```
```java
// 缓存配置工具类
final class CacheConfigurations {
	private static final Map<CacheType, Class<?>> MAPPINGS;
	static {
		Map<CacheType, Class<?>> mappings = new EnumMap<>(CacheType.class);
		mappings.put(CacheType.GENERIC, GenericCacheConfiguration.class);
		mappings.put(CacheType.EHCACHE, EhCacheCacheConfiguration.class);
		mappings.put(CacheType.HAZELCAST, HazelcastCacheConfiguration.class);
		mappings.put(CacheType.INFINISPAN, InfinispanCacheConfiguration.class);
		mappings.put(CacheType.JCACHE, JCacheCacheConfiguration.class);
		mappings.put(CacheType.COUCHBASE, CouchbaseCacheConfiguration.class);
		mappings.put(CacheType.REDIS, RedisCacheConfiguration.class);
		mappings.put(CacheType.CAFFEINE, CaffeineCacheConfiguration.class);
		mappings.put(CacheType.SIMPLE, SimpleCacheConfiguration.class);
		mappings.put(CacheType.NONE, NoOpCacheConfiguration.class);
		MAPPINGS = Collections.unmodifiableMap(mappings);
	}
	private CacheConfigurations() {}
	static String getConfigurationClass(CacheType cacheType) {
		Class<?> configurationClass = MAPPINGS.get(cacheType);
		Assert.state(configurationClass != null, () -> "Unknown cache type " + cacheType);
		return configurationClass.getName();
	}
}
```

* springboot在不导入任何的缓存组件时, 会使用默认的`org.springframework.boot.autoconfigure.cache
.SimpleCacheConfiguration`配置类, 装配一个`org.springframework.cache.concurrent
.ConcurrentMapCacheManager.java`缓存管理器, 再通过缓存管理器的`createConcurrentMapCache
`方法创建真正的缓存对象`ConcurrentMapCache`,其底层就就是维护了一个简单的大小为256的`ConcurrentHashMap`集合.
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CacheManager.class)
@Conditional(CacheCondition.class)
class SimpleCacheConfiguration { 
    @Bean
    ConcurrentMapCacheManager cacheManager(CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers) {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) { 
            cacheManager.setCacheNames(cacheNames); 
        }
        return cacheManagerCustomizers.customize(cacheManager); 
    }
}

public class ConcurrentMapCacheManager implements CacheManager, BeanClassLoaderAware {
    // 通过注解传入的缓存名, 创建缓存对象
    protected Cache createConcurrentMapCache(String name) {
        SerializationDelegate actualSerialization = (isStoreByValue() ? this.serialization : null);
        // 这里维护了一个256的ConcurrentHashMap
        return new ConcurrentMapCache(name, new ConcurrentHashMap<>(256), isAllowNullValues(), actualSerialization); 
    }
}
```
* 可以通过实现`org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer`接口, 来进一步配置缓存管理器
```java
import org.springframework.context.annotation.Configuration;
@Configuration
public class MyConfiguration{
    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                cacheManager.setAllowNullValues(false);
            }
        };
    }
}
```
* 如果在某些环境需要禁用全部缓存, 而强制将缓存类型设置成了`spring.cache.type=none`, springboot
就不会使用默认的`SimpleCacheConfiguration`, 会使用另一个默认的no-op实现`org
.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration`
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(CacheManager.class)
@Conditional(CacheCondition.class)
class NoOpCacheConfiguration {
	@Bean
	NoOpCacheManager cacheManager() {
		return new NoOpCacheManager();
	}
}
public class NoOpCacheManager implements CacheManager {
    // 这里也维护了一个ConcurrentHashMap
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>(16);
	private final Set<String> cacheNames = new LinkedHashSet<>(16);
	public Cache getCache(String name) {
		Cache cache = this.caches.get(name);
		if (cache == null) {
			this.caches.computeIfAbsent(name, key -> new NoOpCache(name));
			synchronized (this.cacheNames) {
				this.cacheNames.add(name);
			}
		}
		return this.caches.get(name);
	}
	public Collection<String> getCacheNames() {
		synchronized (this.cacheNames) {
			return Collections.unmodifiableSet(this.cacheNames);
		}
	}
}
```
* 缓存切面支持类**重要**`org.springframework.cache.interceptor.CacheAspectSupport`, 用来使用解析缓存注解属性值`CacheOperation`
* 具体使用细节请参考[官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-caching)

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
```xml
<dependency>
  <groupId>www.longrise.hello</groupId>
  <artifactId>hello-spring-boot-starter</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

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
* 测试自定义的starter是否成功
    * 配置属性
    ```yaml
    my:
      hello:
        prefix: (
        suffix: )
    ```
    * 编写`MyHelloController`类测试
    ```java
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RestController;
    @RestController
    public class MyHelloController{
        @Autowired
        private MyHelle myHelle;
        @GetMapping("/hello/{name}")
        public String syHello(@PathVariable("name") String name){
            return myHelle.sayHello(name); // 网页输出: (name) 说了句 Hello World!
        }   
    }
    ```


