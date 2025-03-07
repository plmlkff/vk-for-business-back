package ru.itmo.blpslab1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.jwt")
public class JwtProperties {
    private Long lifeTime;
    private String secret;
}
