package ru.itmo.blpslab1.kafka.service

import ru.itmo.blpslab1.kafka.event.dlq.DeadLetterEvent

interface DeadLetterQueueService {
    fun sendDeadLetterInfo(deadLetterEvent: DeadLetterEvent)
}