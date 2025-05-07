package ru.itmo.blpslab1.kafka.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.config.KafkaTopicsConfig
import ru.itmo.blpslab1.kafka.event.subscription.SubscriptionKafkaEvent
import ru.itmo.blpslab1.kafka.service.SubscriptionKafkaEventService

@Service
class SubscriptionKafkaEventServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, SubscriptionKafkaEvent>,
    private val kafkaTopicsConfig: KafkaTopicsConfig
): SubscriptionKafkaEventService {
    private val log: Logger = LoggerFactory.getLogger(SubscriptionKafkaEventServiceImpl::class.java)

    override fun sendSubscriptionEvent(subscriptionKafkaEvent: SubscriptionKafkaEvent) {
        log.info("Publishing new subscription event: $subscriptionKafkaEvent")
        kafkaTemplate.send(kafkaTopicsConfig.subscriptionEvents, subscriptionKafkaEvent)
    }
}