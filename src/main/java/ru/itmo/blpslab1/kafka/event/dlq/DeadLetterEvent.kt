package ru.itmo.blpslab1.kafka.event.dlq

import ru.itmo.blpslab1.kafka.event.KafkaMessage

data class DeadLetterEvent(
    val topic: String,
    val letter: KafkaMessage
): KafkaMessage {
    override val TYPE: String = Companion.TYPE

    companion object{
        const val TYPE = "DeadLetterEvent"
    }
}