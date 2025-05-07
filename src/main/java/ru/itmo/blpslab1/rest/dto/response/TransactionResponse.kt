package ru.itmo.blpslab1.rest.dto.response

import ru.itmo.blpslab1.domain.entity.Transaction
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionState
import ru.itmo.blpslab1.domain.enums.TransactionType
import java.util.*

data class TransactionResponse(
    val id: UUID,
    val transactionType: TransactionType,
    val created: Date,
    val state: TransactionState,
    val actionType: ActionType,
    val amount: Double,
    val targetEntityId: UUID,
    val payer: UserResponse,
    val payerCard: CardCredentialResponse,
    val recipientCard: CardCredentialResponse,
    val paymentLink: String?
)

fun Transaction.toResponse() = TransactionResponse(
    id = id,
    transactionType = transactionType,
    created = created,
    state = state,
    actionType = actionType,
    amount = amount,
    targetEntityId = targetEntityId,
    payer = payer.toResponse(),
    payerCard = payerCard.toResponse(),
    recipientCard = recipientCard.toResponse(),
    paymentLink = null
)