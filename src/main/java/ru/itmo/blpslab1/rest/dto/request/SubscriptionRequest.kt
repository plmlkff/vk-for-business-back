package ru.itmo.blpslab1.rest.dto.request

import ru.itmo.blpslab1.domain.entity.Subscription
import java.util.Date
import java.util.UUID

data class SubscriptionRequest(
    val id: UUID?,
    val from: Date = Date(),
    val to: Date?,
    val tariffId: UUID,
    val ownerId: UUID,
    val ownerCardId: UUID
)

fun SubscriptionRequest.toDomain() = Subscription().also {
    it.id = id
    it.from = from
}
