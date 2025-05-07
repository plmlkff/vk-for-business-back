package ru.itmo.blpslab1.kafka.event.subscription

import java.util.UUID

data class SubscriptionsExpiredEvent(
    val subscriptionIds: List<UUID>
): SubscriptionKafkaEvent {
    override val TYPE: String = Companion.TYPE

    companion object{
        const val TYPE = "SubscriptionExpiredEvent"
    }
}