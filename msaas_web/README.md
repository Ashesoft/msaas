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

## 功能特性
### 对配置文件中敏感的内容做加密的支持
* 使用`jasypt-spring-boot-starter`对配置文件中的敏感信息做了加密, 具体[请了解](https://github.com/Ashesoft/msaas/blob/master/msaas_web/Jasypt%20%E5%9C%A8%20springboot%20%E4%B8%AD%E7%9A%84%E5%88%9D%E6%AD%A5%E4%BD%BF%E7%94%A8.md);
### 对用户访问做鉴权
* 使用`java-jwt`对资源访问做了简单的授权跟鉴权;
### 全局异常处理以及统一的返回数据格式的封装
* 对demo中可能出现的异常做了全局的异常处理, 也对返回的数据进行简单的格式封装;
### 对音乐文件进行解析存库
* 使用`jaudiotagger`对音频文件做简单的处理后存库;
### Request 请求参数的自定义封装
* 了解封装规则
    * 首先在 Controller 层的控制器的请求控制中绑定一个自定义 Java 对象
    ```java
    import org.springframework.web.bind.annotation.RestController;
    
    @RestController
    public class XXXController{
     @PostMapping("/dept/add")
        // EntityBean 继承了 ConcurrentHashMap
        public EntityBean addDept(EntityBean bean){
            return deptService.addDept(Stream.of(bean).collect(EntityBean::new, ConcurrentHashMap::putAll, ConcurrentHashMap::putAll));
        }
    }
    ```
    * 在前端发送请求时会报下面的错
    ```log
    java.lang.IllegalArgumentException: argument type mismatch
    	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
    	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
    	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
    	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
    	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) ~[spring-web-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) ~[spring-web-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at javax.servlet.http.HttpServlet.service(HttpServlet.java:652) ~[tomcat-embed-core-9.0.37.jar:4.0.FR]
    	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.2.8.RELEASE.jar:5.2.8.RELEASE]
    	at javax.servlet.http.HttpServlet.service(HttpServlet.java:733) ~[tomcat-embed-core-9.0.37.jar:4.0.FR]
    	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-9.0.37.jar:9.0.37]
    ```
    * 从上面的异常信息中可以了解到, 最终的错误发生在`InvocableHandlerMethod`这个类`invokeForRequest`方法中:
    ```java
    public class InvocableHandlerMethod extends HandlerMethod {
        private static final Object[] EMPTY_ARGS = new Object[0];
        @Nullable
        private WebDataBinderFactory dataBinderFactory;
        // 方法参数解析器的执行器
        private HandlerMethodArgumentResolverComposite resolvers = new HandlerMethodArgumentResolverComposite();
        private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
  
        @Nullable
        public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
                Object... providedArgs) throws Exception {
            // 获取对请求参数进行解析封装后的值
            Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
            if (logger.isTraceEnabled()) {
                logger.trace("Arguments: " + Arrays.toString(args));
            }
            return doInvoke(args);
        }
        
        // 获取对请求参数进行封装解析后的值
        protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
                Object... providedArgs) throws Exception {
            // 获取方法参数列表
            MethodParameter[] parameters = getMethodParameters();
            if (ObjectUtils.isEmpty(parameters)) {
                return EMPTY_ARGS;
            }
    
            Object[] args = new Object[parameters.length];
            // 循环参数列表, 对每个参数进行对应的解析封装
            for (int i = 0; i < parameters.length; i++) {
                MethodParameter parameter = parameters[i];
                parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
                args[i] = findProvidedArgument(parameter, providedArgs);
                if (args[i] != null) {
                    continue;
                }
                if (!this.resolvers.supportsParameter(parameter)) {
                    throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
                }
                try {
                    // 使用解析器的执行器进行对请求参数的解析封装, 具体如下
                    args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
                }
                catch (Exception ex) {
                    // Leave stack trace for later, exception may actually be resolved and handled...
                    if (logger.isDebugEnabled()) {
                        String exMsg = ex.getMessage();
                        if (exMsg != null && !exMsg.contains(parameter.getExecutable().toGenericString())) {
                            logger.debug(formatArgumentError(parameter, exMsg));
                        }
                    }
                    throw ex;
                }
            }
            return args;
        }
    }
  
    /**
     * 方法参数解析封装的执行器, 26个解析器如下
     * @see RequestParamMethodArgumentResolver
     * @see RequestParamMapMethodArgumentResolver
     * @see PathVariableMethodArgumentResolver
     * @see PathVariableMapMethodArgumentResolver
     * @see MatrixVariableMethodArgumentResolver
     * @see MatrixVariableMapMethodArgumentResolver
     * @see ServletModelAttributeMethodProcessor
     * @see RequestResponseBodyMethodProcessor
     * @see RequestPartMethodArgumentResolver
     * @see RequestHeaderMethodArgumentResolver
     * @see RequestHeaderMapMethodArgumentResolver
     * @see ServletCookieValueMethodArgumentResolver
     * @see ExpressionValueMethodArgumentResolver
     * @see SessionAttributeMethodArgumentResolver 
     * @see RequestAttributeMethodArgumentResolver 
     * @see ServletRequestMethodArgumentResolver 
     * @see ServletResponseMethodArgumentResolver 
     * @see HttpEntityMethodProcessor 
     * @see RedirectAttributesMethodArgumentResolver
     * @see ModelMethodProcessor 
     * @see MapMethodProcessor 
     * @see ErrorsMethodArgumentResolver 
     * @see SessionStatusMethodArgumentResolver
     * @see UriComponentsBuilderMethodArgumentResolver 
     * @see RequestParamMethodArgumentResolver 
     * @see ServletModelAttributeMethodProcessor 
     */
    public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {
    
    	private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();
    
    	private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
    			new ConcurrentHashMap<>(256);
    	// 解析封装请求参数
        @Override
        @Nullable
        public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
            // 通过 HandlerMethodArgumentResolver 解析器中的 supportsParameter 方法获取匹配的解析器
            HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
            if (resolver == null) {
                throw new IllegalArgumentException("Unsupported parameter type [" +
                        parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
            }
            return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        }
        
        // 获取匹配的解析器
        @Nullable
        private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
            HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
            if (result == null) {
                // 通过调式可以知道默认有26个解析器
                for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
                    // 遍历以获取匹配的解析器
                    if (resolver.supportsParameter(parameter)) {
                        result = resolver;
                        this.argumentResolverCache.put(parameter, result);
                        break;
                    }
                }
            }
            return result;
        }
   }
   ```
   * 进入到符合自定义 map 的解析器, 查看具体规则(参数上有注解的)
   ```java
   public class RequestParamMapMethodArgumentResolver implements HandlerMethodArgumentResolver {
      // 判断是否匹配本解析器的要求
      @Override
      public boolean supportsParameter(MethodParameter parameter) {
          // 获取自定义参数上的注解 @RequestParam
          RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
          // 判断是否有指定注解, Map 是否是自定义参数类型的父类或接口或同一个类或同一个接口
          return (requestParam != null && Map.class.isAssignableFrom(parameter.getParameterType()) &&
                  // 判断注解的 name 和 value 值是否有值(hasText)
                  !StringUtils.hasText(requestParam.name()));
      }
      
      // 对请求参数进行解析
      @Override
      public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
              NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
      
          ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
          // 判断 MultiValueMap 是否为自定义参数类的父类或接口, 是否为同一个类或接口, 这里明显都是不是, 进入到常规解析
          if (MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
              // MultiValueMap
              Class<?> valueType = resolvableType.as(MultiValueMap.class).getGeneric(1).resolve();
              if (valueType == MultipartFile.class) {
                  MultipartRequest multipartRequest = MultipartResolutionDelegate.resolveMultipartRequest(webRequest);
                  return (multipartRequest != null ? multipartRequest.getMultiFileMap() : new LinkedMultiValueMap<>(0));
              }
              else if (valueType == Part.class) {
                  HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
                  if (servletRequest != null && MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
                      Collection<Part> parts = servletRequest.getParts();
                      LinkedMultiValueMap<String, Part> result = new LinkedMultiValueMap<>(parts.size());
                      for (Part part : parts) {
                          result.add(part.getName(), part);
                      }
                      return result;
                  }
                  return new LinkedMultiValueMap<>(0);
              }
              else {
                  Map<String, String[]> parameterMap = webRequest.getParameterMap();
                  MultiValueMap<String, String> result = new LinkedMultiValueMap<>(parameterMap.size());
                  parameterMap.forEach((key, values) -> {
                      for (String value : values) {
                          result.add(key, value);
                      }
                  });
                  return result;
              }
          } else {
              // Regular Map 常规处理
              Class<?> valueType = resolvableType.asMap().getGeneric(1).resolve();
              if (valueType == MultipartFile.class) {
                  MultipartRequest multipartRequest = MultipartResolutionDelegate.resolveMultipartRequest(webRequest);
                  return (multipartRequest != null ? multipartRequest.getFileMap() : new LinkedHashMap<>(0));
              } else if (valueType == Part.class) {
                  HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
                  if (servletRequest != null && MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
                      Collection<Part> parts = servletRequest.getParts();
                      LinkedHashMap<String, Part> result = new LinkedHashMap<>(parts.size());
                      for (Part part : parts) {
                          if (!result.containsKey(part.getName())) {
                              result.put(part.getName(), part);
                          }
                      }
                      return result;
                  }
                  return new LinkedHashMap<>(0);
              } else {
                  // 默认将请求的参数封装到 LinkedHashMap, 所以要接收的封装参数为 map 类型, 之后再手动转换成自定义的类型
                  Map<String, String[]> parameterMap = webRequest.getParameterMap();
                  Map<String, String> result = new LinkedHashMap<>(parameterMap.size());
                  parameterMap.forEach((key, values) -> {
                      if (values.length > 0) {
                          result.put(key, values[0]);
                      }
                  });
                  return result;
              }
          }
      }
   }
   ```
  * 如果参数上没有注解呢
  ```java
  public class MapMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {
    // 判断是否匹配本解析器的要求
  	@Override
  	public boolean supportsParameter(MethodParameter parameter) {
        // 判断 Map 是否为自定义参数类型的父类或接口或同一个类或同一个接口
  		return Map.class.isAssignableFrom(parameter.getParameterType()) && 
                // 判断自定义参数上是否有注解, 没有注解为真
  				parameter.getParameterAnnotations().length == 0;
  	}
  
  	@Override
  	@Nullable
  	public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
  			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
  
  		Assert.state(mavContainer != null, "ModelAndViewContainer is required for model exposure");
  		return mavContainer.getModel(); // 返回一个没有视图名也没有视图属性的 ModelMap
  	}
  }
  ```
  * 其他解析器查看同理(略)
* 了解了默认的一些封装规则, 如何自定义一个封装规则呢, 如下:
    * 根据默认的一些规则, 具体请参考`HandlerMethodArgumentResolver`接口及其实现类, 找到合适的匹配条件并满足它, 如: 在参数上使用注解等
    * 手动编码添加一个解析器, 如下:
    ```java
    import com.longrise.msaas.global.domain.EntityBean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.MethodParameter;
    import org.springframework.web.bind.support.WebDataBinderFactory;
    import org.springframework.web.context.request.NativeWebRequest;
    import org.springframework.web.method.support.HandlerMethodArgumentResolver;
    import org.springframework.web.method.support.ModelAndViewContainer;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    import org.springframework.web.method.annotation.MapMethodProcessor;
    import java.util.List;
    import java.util.Map;
    /**
     * 使用自定义的参数封装解析器, 必须配合一个任意自定义的注解使用, 具体请查看 MapMethodProcessor 类中的 supportsParameter 方法
     * @see  MapMethodProcessor
     */
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


