package com.example.biblio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*

    @Configuration
    @EnableWebMvc
    public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**") // Apply CORS to all /api/ routes
                    .allowedOrigins("http://localhost:4200") // Allow frontend domain
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow all HTTP methods
                    .allowedHeaders("Authorization", "Content-Type") // Allow necessary headers
                    .allowCredentials(true); // Allow credentials
        }

    }

*/