package com.daniel.community.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 외부에서 접근할 URL 패턴
    private static final String PROFILE_IMAGE_URL_PATTERN = "/images/profiles/**";
    private static final String POST_IMAGE_URL_PATTERN = "/images/posts/**";

    // 실제 파일이 저장되는 로컬 폴더
    private static final String PROFILE_UPLOAD_DIRECTORY = "uploads/profiles";
    private static final String POST_UPLOAD_DIRECTORY = "uploads/posts";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        addFileResourceHandler(
                registry,
                PROFILE_IMAGE_URL_PATTERN,
                PROFILE_UPLOAD_DIRECTORY
        );
        addFileResourceHandler(
                registry,
                POST_IMAGE_URL_PATTERN,
                POST_UPLOAD_DIRECTORY
        );
    }

    private void addFileResourceHandler(
            ResourceHandlerRegistry registry,
            String urlPattern,
            String uploadDirectory
    ) {
        String uploadPath = Path.of(uploadDirectory)
                .toAbsolutePath()
                .normalize()
                .toString();

        registry.addResourceHandler(urlPattern)
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