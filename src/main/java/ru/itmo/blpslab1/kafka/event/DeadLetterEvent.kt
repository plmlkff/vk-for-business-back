package ru.itmo.blpslab1.kafka.event

data class DeadLetterEvent(
    val topic: String,
    val letter: KafkaMessage
): KafkaMessage{
    override val TYPE: String = DeadLetterEvent.TYPE

    companion object{
        const val TYPE = "DeadLetterEvent"
    }
}