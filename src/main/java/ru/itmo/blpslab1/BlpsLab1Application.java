package ru.itmo.blpslab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaRepositories
@EnableRetry
@EnableConfigurationProperties
public class BlpsLab1Application {

    public static void main(String[] args) {
        SpringApplication.run(BlpsLab1Application.class, args);
    }

}
