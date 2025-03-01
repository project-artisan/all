package org.artisan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 엔드포인트에 대해
                        .allowedOrigins("*") // 모든 출처 허용 (단, allowCredentials(true) 사용 시엔 "*" 대신 명시적 도메인 사용)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)   // 인증 정보 포함 여부 (쿠키, 인증 헤더 등)
                        .maxAge(3600);            // preflight 요청의 캐싱 시간 (초 단위)
            }
        };
    }
}
