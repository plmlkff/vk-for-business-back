package ru.itmo.blpslab1.rest.dto.request

import ru.itmo.blpslab1.domain.entity.Transaction
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionType
import java.util.UUID

data class TransactionRequest(
    val id: UUID?,
    val transactionType: TransactionType,
    val actionType: ActionType,
    val amount: Double,
    val targetEntityId: UUID?,
    val payerId: UUID,
    val payerCardId: UUID,
    val recipientCardId: UUID
)

fun TransactionRequest.toDomain() = Transaction().also {
    it.id = id
    it.transactionType = transactionType
    it.actionType = actionType
    it.amount = amount
    it.targetEntityId = targetEntityId
}