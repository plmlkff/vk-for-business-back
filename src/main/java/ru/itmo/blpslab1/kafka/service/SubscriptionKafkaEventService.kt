package ru.itmo.blpslab1.kafka.service

import ru.itmo.blpslab1.kafka.event.subscription.SubscriptionKafkaEvent

interface SubscriptionKafkaEventService {
    fun sendSubscriptionEvent(subscriptionKafkaEvent: SubscriptionKafkaEvent)
}