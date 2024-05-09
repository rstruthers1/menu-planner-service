package com.homemenuplanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origin}")
    private String allowedOrigin; // This will pull the 'cors.allowed.origin' from your application.properties or environment


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // You can restrict paths with specific ones like '/api/**'
                .allowedOrigins(allowedOrigin) // Add the origins you want to allow
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Optionally add more HTTP methods to allow
                .allowedHeaders("*") // Allows all headers
                .allowCredentials(true);
    }
}
