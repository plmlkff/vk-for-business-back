package ru.itmo.blpslab1.rest.dto.response

import ru.itmo.blpslab1.domain.entity.Subscription
import java.util.Date
import java.util.UUID

data class SubscriptionResponse(
    val id: UUID?,
    val from: Date,
    val to: Date,
    val isPaid: Boolean,
    val tariff: TariffResponse,
    val owner: UserResponse,
    val paymentUrl: String?
)

fun Subscription.toResponse() = SubscriptionResponse(
    id = id,
    from = from,
    to = to,
    isPaid = isPaid,
    tariff = tariff.toResponse(),
    owner = owner.toResponse(),
    paymentUrl = null
)
