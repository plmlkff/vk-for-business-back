package ru.itmo.blpslab1.kafka.event.transaction

import ru.itmo.blpslab1.domain.entity.Transaction
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionState
import ru.itmo.blpslab1.domain.enums.TransactionType
import ru.itmo.blpslab1.kafka.event.KafkaMessage
import java.util.UUID

data class TransactionEvent(
    val actionType: ActionType,
    val targetEntityId: UUID,
    val transactionType: TransactionType,
    val amount: Double,
    val transactionState: TransactionState
): KafkaMessage {
    override val TYPE: String = Companion.TYPE

    companion object{
        const val TYPE = "TransactionEvent"
    }
}

fun Transaction.toKafkaEvent(): TransactionEvent = TransactionEvent(
    actionType = this.actionType,
    targetEntityId = this.targetEntityId,
    transactionType = this.transactionType,
    amount = this.amount,
    transactionState = this.state
)