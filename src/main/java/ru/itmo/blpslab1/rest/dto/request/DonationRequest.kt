package ru.itmo.blpslab1.rest.dto.request

import java.util.UUID

data class OnceDonationRequest(
    val amount: Double,
    val payerCardId: UUID,
    val payerId: UUID,
    val recipientCardId: UUID
)

data class GoalDonationRequest(
    val amount: Double,
    val payerId: UUID,
    val payerCardId: UUID,
    val goalId: UUID
)
