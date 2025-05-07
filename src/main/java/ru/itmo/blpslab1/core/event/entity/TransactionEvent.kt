package ru.itmo.blpslab1.core.event.entity

import org.springframework.context.ApplicationEvent
import ru.itmo.blpslab1.domain.entity.Transaction

data class TransactionChangedEvent(
    val transaction: Transaction
): ApplicationEvent(transaction)
