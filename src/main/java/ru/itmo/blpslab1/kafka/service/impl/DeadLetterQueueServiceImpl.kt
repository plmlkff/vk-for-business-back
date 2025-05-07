package ru.itmo.blpslab1.kafka.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.config.KafkaTopicsConfig
import ru.itmo.blpslab1.kafka.event.dlq.DeadLetterEvent
import ru.itmo.blpslab1.kafka.service.DeadLetterQueueService

@Service
class DeadLetterQueueServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val kafkaTopicsConfig: KafkaTopicsConfig
): DeadLetterQueueService {
    val log: Logger = LoggerFactory.getLogger(DeadLetterQueueServiceImpl::class.java)

    override fun sendDeadLetterInfo(deadLetterEvent: DeadLetterEvent) {
        log.info("Publishing new death letter: $deadLetterEvent")
        kafkaTemplate.send(kafkaTopicsConfig.deathLetterQueue, deadLetterEvent)
    }

}