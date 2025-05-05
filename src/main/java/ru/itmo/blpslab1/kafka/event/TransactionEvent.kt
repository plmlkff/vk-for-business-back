package ru.itmo.blpslab1.kafka.event

import ru.itmo.blpslab1.domain.entity.Transaction
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionType
import java.util.UUID

data class TransactionEvent(
    val actionType: ActionType,
    val targetEntityId: UUID,
    val transactionType: TransactionType,
    val amount: Double
): KafkaMessage{
    override val TYPE: String = TransactionEvent.TYPE

    companion object{
        const val TYPE = "TransactionEvent"
    }
}

fun Transaction.toKafkaEvent(): TransactionEvent = TransactionEvent(
    actionType = this.actionType,
    targetEntityId = this.targetEntityId,
    transactionType = this.transactionType,
    amount = this.amount
)