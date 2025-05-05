package ru.itmo.blpslab1.kafka.consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.config.KafkaTopicsConfig
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.kafka.event.DeadLetterEvent
import ru.itmo.blpslab1.kafka.event.TransactionEvent
import ru.itmo.blpslab1.kafka.service.DeadLetterQueueService
import ru.itmo.blpslab1.rest.dto.request.GoalAmountChangeRequest
import ru.itmo.blpslab1.service.GoalService
import ru.itmo.blpslab1.service.SubscriptionService
import ru.itmo.blpslab1.utils.service.Result
import ru.itmo.blpslab1.utils.service.ok

@Service
class TransactionEventConsumer(
    private val goalService: GoalService,
    private val subscriptionService: SubscriptionService,
    private val deadLetterQueueService: DeadLetterQueueService,
    private val kafkaTopicsConfig: KafkaTopicsConfig
){
    private val log: Logger = LoggerFactory.getLogger(TransactionEventConsumer::class.java)

    @KafkaListener(topics = ["\${kafka-topics.transaction-events}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun handleTransactionEvent(transactionEvent: TransactionEvent){
        log.info("Received new event: $transactionEvent")
        val res = processTransactionEvent(transactionEvent)
        if (res.isError()) deadLetterQueueService.sendDeathLetterInfo(DeadLetterEvent(kafkaTopicsConfig.transactionEvents, transactionEvent))
    }

    private fun processTransactionEvent(transactionEvent: TransactionEvent): Result<*>{
        return when(transactionEvent.actionType) {
            ActionType.GOAL -> invokeGoalTransactionProcessing(transactionEvent)
            ActionType.SUBSCRIPTION -> invokeSubscriptionTransactionProcessing(transactionEvent)
            ActionType.DONATION -> ok<Nothing>()
        }
    }

    private fun invokeGoalTransactionProcessing(
        transactionEvent: TransactionEvent
    ) = goalService.editGoalAmount(
        GoalAmountChangeRequest(
            goalId = transactionEvent.targetEntityId,
            transactionType = transactionEvent.transactionType,
            amount = transactionEvent.amount
        )
    )

    private fun invokeSubscriptionTransactionProcessing(
        transactionEvent: TransactionEvent
    ) = subscriptionService.markSubscriptionPaid(
        transactionEvent.targetEntityId
    )
}