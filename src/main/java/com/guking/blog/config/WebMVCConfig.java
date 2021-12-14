package com.guking.blog.config;

import com.guking.blog.hander.LoginIntercetor;
import com.guking.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginIntercetor loginIntercetor;


    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // 跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
        // 本地测试 端口不一致 也算跨域
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // todo 拦截test接口,后续实际遇到需要拦截的接口时,再做调整
        registry.addInterceptor(loginIntercetor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}
