package com.ttcs.ttcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    private final AuthInterceptor authInterceptor;
    public WebConfig(AuthInterceptor authInterceptor){
        this.authInterceptor = authInterceptor;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/customer/**",
                        "/staff/**",
                        "/admin/**"
                )
                .excludePathPatterns(
                        "/",
                        "/login",
                        "/register",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );

    }

}

