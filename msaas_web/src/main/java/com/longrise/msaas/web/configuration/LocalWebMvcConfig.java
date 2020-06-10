package com.longrise.msaas.web.configuration;

import com.longrise.msaas.web.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LocalWebMvcConfig implements WebMvcConfigurer {
    @Value("${address.audio.intercept}")
    private String audioIntercept;
    @Value("${address.audio.redirect}")
    private String audioRedirect;

    @Autowired
    private Environment env;

    /**
     * 将服务器请求资源的地址映射到本地路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(audioIntercept).addResourceLocations(audioRedirect);
        registry.addResourceHandler(env.getProperty("address.video.intercept")).addResourceLocations(env.getProperty("address.video.redirect"));
    }

    /**
     * 添加拦截机制
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor(){
        return new AuthorizationInterceptor();
    }
}
