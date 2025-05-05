package ru.itmo.blpslab1.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kafka-topics")
class KafkaTopicsConfig {
    lateinit var transactionEvents: String
}