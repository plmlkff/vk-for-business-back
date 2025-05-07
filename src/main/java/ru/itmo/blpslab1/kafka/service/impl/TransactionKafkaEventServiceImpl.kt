package ru.itmo.blpslab1.kafka.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.config.KafkaTopicsConfig
import ru.itmo.blpslab1.kafka.event.KafkaMessage
import ru.itmo.blpslab1.kafka.event.transaction.TransactionEvent
import ru.itmo.blpslab1.kafka.service.TransactionKafkaEventService

@Service
class TransactionKafkaEventServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, KafkaMessage>,
    private val kafkaTopicsConfig: KafkaTopicsConfig
): TransactionKafkaEventService {
    private val log: Logger = LoggerFactory.getLogger(TransactionKafkaEventServiceImpl::class.java)

    override fun publishEvent(transactionEvent: TransactionEvent) {
        kafkaTemplate.send(kafkaTopicsConfig.transactionEvents, transactionEvent);
        log.info("Published new event: $transactionEvent")
    }
}