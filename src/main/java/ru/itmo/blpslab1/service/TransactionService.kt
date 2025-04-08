package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.domain.enums.TransactionState
import ru.itmo.blpslab1.rest.dto.request.GoalDonationRequest
import ru.itmo.blpslab1.rest.dto.request.OnceDonationRequest
import ru.itmo.blpslab1.rest.dto.request.TransactionRequest
import ru.itmo.blpslab1.rest.dto.response.TransactionResponse
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID

interface TransactionService {
    fun createTransaction(
        userDetails: UserDetails,
        transactionRequest: TransactionRequest
    ): Result<TransactionResponse>

    @PreAuthorize("hasAnyAuthority('TRANSACTION_CREATE', 'TRANSACTION_ADMIN')")
    fun getTransaction(
        userDetails: UserDetails,
        id: UUID
    ): Result<TransactionResponse>

    @PreAuthorize("hasAnyAuthority('TRANSACTION_ADMIN')")
    fun editTransactionState(
        userDetails: UserDetails,
        id: UUID,
        newState: TransactionState
    ): Result<TransactionResponse>

    @PreAuthorize("hasAnyAuthority('TRANSACTION_CREATE', 'TRANSACTION_ADMIN')")
    fun createOnceDonation(userDetails: UserDetails, onceDonationRequest: OnceDonationRequest): Result<TransactionResponse>

    @PreAuthorize("hasAnyAuthority('TRANSACTION_CREATE', 'TRANSACTION_ADMIN')")
    fun createGoalDonation(userDetails: UserDetails, goalDonationRequest: GoalDonationRequest): Result<TransactionResponse>
}