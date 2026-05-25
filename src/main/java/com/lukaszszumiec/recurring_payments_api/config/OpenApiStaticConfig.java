package com.lukaszszumiec.recurring_payments_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiStaticConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("OpenApiStaticConfig loaded!");

        registry.addResourceHandler("/openapi.yaml")
                .addResourceLocations("classpath:/openapi.yaml");
    }
}
