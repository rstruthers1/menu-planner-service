package com.homemenuplanner;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MainClass {

    public static void main(String[] args) {
        // Check if the application is running in production
        String env = System.getenv("SPRING_PROFILES_ACTIVE");
        if (env == null || !env.equals("prod")) {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("spring.datasource.url", dotenv.get("JDBC_URL"));
            System.setProperty("spring.datasource.username", dotenv.get("JDBC_USERNAME"));
            System.setProperty("spring.datasource.password", dotenv.get("JDBC_PASSWORD"));
            System.setProperty("jwt.secret", dotenv.get("JWT_SECRET"));
            System.setProperty("jwt.cookie.secure", dotenv.get("JWT_COOKIE_SECURE"));
        }
        SpringApplication.run(MainClass.class, args);
    }

}
