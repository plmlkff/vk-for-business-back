package ru.itmo.blpslab1.service.impl

import org.springframework.http.HttpStatus.*
import kotlin.jvm.optionals.getOrNull

import ru.itmo.blpslab1.utils.service.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.entity.Tariff
import ru.itmo.blpslab1.domain.enums.ActionType
import ru.itmo.blpslab1.domain.enums.TransactionType
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.domain.repository.CardCredentialRepository
import ru.itmo.blpslab1.domain.repository.SubscriptionRepository
import ru.itmo.blpslab1.domain.repository.TariffRepository
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.rest.dto.request.SubscriptionRequest
import ru.itmo.blpslab1.rest.dto.request.TransactionRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.SubscriptionResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.SubscriptionService
import ru.itmo.blpslab1.service.TransactionService
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.core.hasNoAuthority
import ru.itmo.blpslab1.utils.core.test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class SubscriptionServiceImpl(
    private val subscriptionRepository: SubscriptionRepository,
    private val tariffRepository: TariffRepository,
    private val userRepository: UserRepository,
    private val cardCredentialRepository: CardCredentialRepository,
    private val transactionService: TransactionService
): SubscriptionService {

    @Transactional
    override fun createSubscription(
        userDetails: UserDetails,
        subscriptionRequest: SubscriptionRequest
    ): Result<SubscriptionResponse> {
        if (subscriptionRequest.id != null) return error()

        val tariff = tariffRepository.findById(subscriptionRequest.tariffId).getOrNull() ?: return error(NOT_FOUND)
        val owner = tariff.group.owner
        val ownerCard = cardCredentialRepository.findById(subscriptionRequest.ownerCardId).getOrNull() ?: return error(NOT_FOUND)

        if (owner.login != userDetails.username
            && userDetails hasNoAuthority UserAuthority.SUBSCRIPTION_ADMIN) return error(METHOD_NOT_ALLOWED)

        var subscription = subscriptionRequest.toDomain().apply {
            to = Date.from(ZonedDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault()).plusMonths(1).toInstant())
            this.tariff = tariff
            this.owner = owner
        }

        subscription = subscriptionRepository.save(subscription)
        val transactionRequest = createTransactionRequest(tariff, subscription.id, owner.id, ownerCard.id)

        val createTransactionRes = transactionService.createTransaction(userDetails, transactionRequest)

        if (createTransactionRes.status != OK) throw RollbackTransactionException(createTransactionRes.status)

        return ok(subscription.toResponse().copy(paymentUrl = createTransactionRes.body?.paymentLink))
    }

    override fun getSubscription(
        userDetails: UserDetails,
        id: UUID
    ) = subscriptionRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = {
            if (userDetails hasNoAccessTo it!!) return@test error(METHOD_NOT_ALLOWED)
            else ok(it.toResponse())
        },
        onFalse = { error(NOT_FOUND) }
    )

    @Transactional
    override fun editSubscription(
        userDetails: UserDetails,
        subscriptionRequest: SubscriptionRequest
    ): Result<SubscriptionResponse> {
        if (subscriptionRequest.id == null) return error()

        val dbSubscription = subscriptionRepository.findById(subscriptionRequest.id).getOrNull() ?: return error(NOT_FOUND)
        val newOwner = userRepository.findById(subscriptionRequest.ownerId).getOrNull() ?: return error(NOT_FOUND)
        val newTariff = tariffRepository.findById(subscriptionRequest.tariffId).getOrNull() ?: return error(NOT_FOUND)
        val newSubscription = subscriptionRequest.toDomain().apply {
            owner = newOwner
            tariff = newTariff
            to = subscriptionRequest.to ?: dbSubscription.to
        }

        return ok(subscriptionRepository.save(newSubscription).toResponse())
    }

    @Transactional
    override fun removeSubscription(
        userDetails: UserDetails,
        id: UUID
    ) = subscriptionRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = {
            if (userDetails hasNoAccessTo it!!) error(METHOD_NOT_ALLOWED)
            else ok(subscriptionRepository.delete(it))
        },
        onFalse = { error(NOT_FOUND) }
    )

    override fun getAllByOwner(
        userDetails: UserDetails
    ): Result<List<SubscriptionResponse>> = ok(
        subscriptionRepository.findAllByOwner(userDetails.username)
            .map { it.toResponse() }
    )

    @Transactional
    override fun markSubscriptionPaid(
        id: UUID
    ) = subscriptionRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = { ok(subscriptionRepository.save(it!!.apply { isPaid = true }).toResponse()) },
        onFalse = { error(NOT_FOUND) }
    )

    private fun createTransactionRequest(
        tariff: Tariff, subscriptionId: UUID, payerId: UUID, payerCardId: UUID
    ) = TransactionRequest(
        id = null,
        transactionType = TransactionType.DEBIT,
        actionType = ActionType.SUBSCRIPTION,
        amount = tariff.price,
        targetEntityId = subscriptionId,
        payerId = payerId,
        payerCardId = payerCardId,
        recipientCardId = tariff.recipientCard.id
    )
}