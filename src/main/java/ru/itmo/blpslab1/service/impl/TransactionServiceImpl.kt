package ru.itmo.blpslab1.service.impl

import org.springframework.http.HttpStatus.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionState
import ru.itmo.blpslab1.domain.enums.TransactionType
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.domain.repository.CardCredentialRepository
import ru.itmo.blpslab1.domain.repository.GoalRepository
import ru.itmo.blpslab1.domain.repository.TransactionRepository
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.kafka.event.toKafkaEvent
import ru.itmo.blpslab1.kafka.service.TransactionKafkaEventService
import ru.itmo.blpslab1.rest.dto.request.GoalDonationRequest
import ru.itmo.blpslab1.rest.dto.request.OnceDonationRequest
import ru.itmo.blpslab1.rest.dto.request.TransactionRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.TransactionResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.TransactionService
import ru.itmo.blpslab1.service.paymentgateway.PaymentGatewayService
import ru.itmo.blpslab1.utils.core.hasNoAuthority
import ru.itmo.blpslab1.utils.core.test
import ru.itmo.blpslab1.utils.core.toResponse
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID
import ru.itmo.blpslab1.utils.service.*
import kotlin.jvm.optionals.getOrNull

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val cardCredentialRepository: CardCredentialRepository,
    private val paymentGatewayService: PaymentGatewayService,
    private val goalRepository: GoalRepository,
    private val transactionKafkaEventService: TransactionKafkaEventService
) : TransactionService {

    @Transactional
    override fun createTransaction(
        userDetails: UserDetails,
        transactionRequest: TransactionRequest
    ): Result<TransactionResponse> {
        if (transactionRequest.id != null) return error()

        val dbPayer = userRepository.findById(transactionRequest.payerId).getOrNull() ?: return error(NOT_FOUND)
        val dbPayerCard = cardCredentialRepository.findById(transactionRequest.payerCardId).getOrNull() ?: return error(NOT_FOUND)
        val dbRecipientCard = cardCredentialRepository.findById(transactionRequest.recipientCardId).getOrNull() ?: return error(NOT_FOUND)

        val filledTransaction = transactionRequest.toDomain().apply {
            payer = dbPayer
            payerCard = dbPayerCard
            recipientCard = dbRecipientCard
        }

        transactionRepository.save(filledTransaction)

        val link = paymentGatewayService.registerPayment(filledTransaction)

        return ok(filledTransaction.toResponse().copy(paymentLink = link))
    }

    override fun getTransaction(
        userDetails: UserDetails,
        id: UUID
    ) = transactionRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = { ok(it!!.toResponse()) },
        onFalse = { error(NOT_FOUND) }
    )

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override fun editTransactionState(
        userDetails: UserDetails,
        id: UUID,
        newState: TransactionState
    ): Result<TransactionResponse> {
        if (userDetails hasNoAuthority UserAuthority.TRANSACTION_ADMIN) return error(METHOD_NOT_ALLOWED)

        val dbTransaction = transactionRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        if (dbTransaction.state != TransactionState.NEW) return ok(dbTransaction.toResponse())

        val newTransaction = dbTransaction.apply { state = newState }

        transactionKafkaEventService.publishEvent(newTransaction.toKafkaEvent())
        return ok(transactionRepository.save(newTransaction).toResponse())
    }


    override fun createOnceDonation(userDetails: UserDetails, onceDonationRequest: OnceDonationRequest): Result<TransactionResponse> {
        val payer = userRepository.findById(onceDonationRequest.payerId).getOrNull() ?: kotlin.error(NOT_FOUND)
        if (!payer.credentials.map { it.id }.contains(onceDonationRequest.payerCardId)) return error(METHOD_NOT_ALLOWED)
        if (!cardCredentialRepository.existsById(onceDonationRequest.recipientCardId)) return error(METHOD_NOT_ALLOWED)

        val transactionRequest = TransactionRequest(
            id = null,
            transactionType = TransactionType.DEBIT,
            actionType = ActionType.DONATION,
            amount = onceDonationRequest.amount,
            targetEntityId = null,
            payerId = onceDonationRequest.payerId,
            payerCardId = onceDonationRequest.payerCardId,
            recipientCardId = onceDonationRequest.recipientCardId
        )

        return createTransaction(userDetails, transactionRequest);
    }

    override fun createGoalDonation(userDetails: UserDetails, goalDonationRequest: GoalDonationRequest): Result<TransactionResponse> {
        val payer = userRepository.findById(goalDonationRequest.payerId).getOrNull() ?: return error(NOT_FOUND)
        if (!payer.credentials.map { it.id }.contains(goalDonationRequest.payerCardId)) return error(NOT_FOUND)
        val goal = goalRepository.findById(goalDonationRequest.goalId).getOrNull() ?: return error(NOT_FOUND)

        val transactionRequest = TransactionRequest(
            id = null,
            transactionType = TransactionType.DEBIT,
            actionType = ActionType.GOAL,
            amount = goalDonationRequest.amount,
            targetEntityId = goal.id,
            payerId = goalDonationRequest.payerId,
            payerCardId = goalDonationRequest.payerCardId,
            recipientCardId = goal.recipientCard.id
        )

        return createTransaction(userDetails, transactionRequest);
    }
}