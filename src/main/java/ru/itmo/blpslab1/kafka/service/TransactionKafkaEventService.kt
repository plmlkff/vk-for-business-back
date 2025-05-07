package ru.itmo.blpslab1.kafka.service

import ru.itmo.blpslab1.kafka.event.transaction.TransactionEvent

interface TransactionKafkaEventService {
    fun publishEvent(transactionEvent: TransactionEvent)
}