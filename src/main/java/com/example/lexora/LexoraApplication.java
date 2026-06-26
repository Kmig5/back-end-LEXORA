package com.example.lexora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.example.lexora")
@ComponentScan(basePackages = "com.example.lexora")
public class LexoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LexoraApplication.class, args);
    }

}
