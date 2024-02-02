package com.jameswu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@SpringBootApplication
@EntityScan(basePackages = {"com.jameswu.demo.model"})
@ComponentScan(
        basePackages = {
            "com.jameswu.demo.config",
            "com.jameswu.demo.controller",
            "com.jameswu.demo.exception",
            "com.jameswu.demo.filter",
            "com.jameswu.demo.service",
            "com.jameswu.demo.notification",
            "com.jameswu.demo.repository"
        })
@EnableJpaRepositories(basePackages = "com.jameswu.demo.repository")
@EnableTransactionManagement
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
