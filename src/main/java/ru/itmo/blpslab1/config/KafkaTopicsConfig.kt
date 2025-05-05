package ru.itmo.blpslab1.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kafka-topics")
class KafkaTopicsConfig {
    lateinit var transactionEvents: String
    lateinit var deathLetterQueue: String
}