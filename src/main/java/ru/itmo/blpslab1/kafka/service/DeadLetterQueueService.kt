package ru.itmo.blpslab1.kafka.service

import ru.itmo.blpslab1.kafka.event.DeadLetterEvent

interface DeadLetterQueueService {
    fun sendDeathLetterInfo(deadLetterEvent: DeadLetterEvent)
}