package com.evereading.best.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.evereading.best") // 프로젝트의 루트 패키지를 스캔
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/thumbnail/**")
                .addResourceLocations("file:/C:/novelThumbnail/"); // 로컬 이미지 디렉토리 경로

        registry.addResourceHandler("/carouselImg/**")
                .addResourceLocations("file:/C:/carouselImg/");

        registry.addResourceHandler("/eventImg/**")
                .addResourceLocations("file:/C:/eventImg/");
    }
}
