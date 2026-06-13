package com.daniel.community.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 외부에서 접근할 URL 패턴
    private static final String IMAGE_URL_PATTERN = "/images/profiles/**";
    // 실제 파일이 저장되는 로컬 폴더
    private static final String UPLOAD_DIRECTORY = "uploads/profiles";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 파일의 경로를 절대 경로로 저장
        String uploadPath = Path.of(UPLOAD_DIRECTORY)
                .toAbsolutePath()
                .normalize()
                .toString();

        // URL 패턴을 등록
        registry.addResourceHandler(IMAGE_URL_PATTERN)
                // 실제 파일 위치를 등록
                .addResourceLocations("file:" + uploadPath + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 API 경로에 CORS 설정 적용
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://localhost:5500",
                        "http://127.0.0.1:5500",
                        "http://localhost:8080",
                        "http://127.0.0.1:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(false)
                .maxAge(3600);
    }
}