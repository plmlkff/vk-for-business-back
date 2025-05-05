package ru.itmo.blpslab1.kafka.consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.kafka.event.TransactionEvent
import ru.itmo.blpslab1.rest.dto.request.GoalAmountChangeRequest
import ru.itmo.blpslab1.service.GoalService
import ru.itmo.blpslab1.service.SubscriptionService

@Service
class TransactionEventConsumer(
    private val goalService: GoalService,
    private val subscriptionService: SubscriptionService
){
    private val log: Logger = LoggerFactory.getLogger(TransactionEventConsumer::class.java)

    private object SuperAdmin : UserDetails {
        private fun readResolve(): Any = SuperAdmin

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
            return UserAuthority.entries.toMutableSet()
        }

        override fun getPassword(): String = ""

        override fun getUsername(): String = ""

    }

    @KafkaListener(topics = ["\${kafka-topics.transaction-events}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun handleTransactionEvent(transactionEvent: TransactionEvent){
        log.info("Received new event: $transactionEvent")
        processTransactionEvent(transactionEvent)
    }

    private fun processTransactionEvent(transactionEvent: TransactionEvent){
        when(transactionEvent.actionType) {
            ActionType.GOAL -> invokeGoalTransactionProcessing(transactionEvent)
            ActionType.SUBSCRIPTION -> invokeSubscriptionTransactionProcessing(transactionEvent)
            ActionType.DONATION -> Unit
        }
    }

    private fun invokeGoalTransactionProcessing(
        transactionEvent: TransactionEvent
    ) = goalService.editGoalAmount(
        SuperAdmin, GoalAmountChangeRequest(
            goalId = transactionEvent.targetEntityId,
            transactionType = transactionEvent.transactionType,
            amount = transactionEvent.amount
        )
    )

    private fun invokeSubscriptionTransactionProcessing(
        transactionEvent: TransactionEvent
    ) = subscriptionService.markSubscriptionPaid(
        SuperAdmin, transactionEvent.targetEntityId
    )
}