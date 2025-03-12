package ru.itmo.blpslab1.rest.dto.request

import ru.itmo.blpslab1.domain.enums.TransactionType
import java.util.UUID

data class GoalAmountChangeRequest(
    val goalId: UUID,
    val transactionType: TransactionType,
    val amount: Double
)