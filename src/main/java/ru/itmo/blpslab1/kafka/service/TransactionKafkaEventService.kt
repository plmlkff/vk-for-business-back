package ru.itmo.blpslab1.kafka.service

import ru.itmo.blpslab1.kafka.event.TransactionEvent

interface TransactionKafkaEventService {
    fun publishEvent(transactionEvent: TransactionEvent)
}