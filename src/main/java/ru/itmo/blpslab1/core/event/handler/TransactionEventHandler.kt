package ru.itmo.blpslab1.core.event.handler

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.itmo.blpslab1.core.event.entity.TransactionEvent

@Component
class TransactionEventHandler {

    @EventListener
    fun onTransactionEvent(transactionEvent: TransactionEvent){

    }
}