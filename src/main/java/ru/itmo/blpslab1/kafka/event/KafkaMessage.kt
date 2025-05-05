package ru.itmo.blpslab1.kafka.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(value =  [
    JsonSubTypes.Type(value = TransactionEvent::class, name = TransactionEvent.TYPE),
    JsonSubTypes.Type(value = DeadLetterEvent::class, name = DeadLetterEvent.TYPE)
])
interface KafkaMessage{
    val TYPE: String
}