package com.jameswu.security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@SpringBootApplication
@EntityScan(basePackages = {"com.jameswu.security.demo.model"})
@ComponentScan(
        basePackages = {
            "com.jameswu.security.demo.service",
            "com.jameswu.security.demo.config",
            "com.jameswu.security.demo.controller"
        })
@EnableJpaRepositories(basePackages = "com.jameswu.security.demo.repository")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
