package ru.itmo.blpslab1.core.event.handler

import org.springframework.context.event.EventListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import ru.itmo.blpslab1.core.event.entity.TransactionChangedEvent
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionState
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.rest.dto.request.GoalAmountChangeRequest
import ru.itmo.blpslab1.service.GoalService
import ru.itmo.blpslab1.service.SubscriptionService

@Component
class TransactionEventHandler(
    private val goalService: GoalService,
    private val subscriptionService: SubscriptionService
) {
    private object SuperAdmin : UserDetails {
        private fun readResolve(): Any = SuperAdmin

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
            return UserAuthority.entries.toMutableSet()
        }

        override fun getPassword(): String = ""

        override fun getUsername(): String = ""

    }

    @EventListener
    fun onTransactionChangedEvent(transactionChangedEvent: TransactionChangedEvent) {
        if (transactionChangedEvent.transaction.state == TransactionState.CANCELED) Unit
        else when (transactionChangedEvent.transaction.actionType) {
            ActionType.GOAL -> {
                invokeGoalTransactionProcessing(transactionChangedEvent)
            }
            ActionType.SUBSCRIPTION -> {
                invokeSubscriptionTransactionProcessing(transactionChangedEvent)
            }
            ActionType.DONATION -> Unit
        }
    }

    private fun invokeGoalTransactionProcessing(
        transactionChangedEvent: TransactionChangedEvent
    ) = goalService.editGoalAmount(
        SuperAdmin, GoalAmountChangeRequest(
            goalId = transactionChangedEvent.transaction.targetEntityId,
            transactionType = transactionChangedEvent.transaction.transactionType,
            amount = transactionChangedEvent.transaction.amount
        )
    )

    private fun invokeSubscriptionTransactionProcessing(
        transactionChangedEvent: TransactionChangedEvent
    ) = subscriptionService.markSubscriptionPaid(
        SuperAdmin, transactionChangedEvent.transaction.targetEntityId
    )

}