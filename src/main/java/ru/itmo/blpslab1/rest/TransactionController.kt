package ru.itmo.blpslab1.rest

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.blpslab1.rest.dto.request.GoalDonationRequest
import ru.itmo.blpslab1.rest.dto.request.OnceDonationRequest
import ru.itmo.blpslab1.service.TransactionService
import java.util.UUID

@RestController
@RequestMapping("/api/transaction")
class TransactionController(
    private val transactionService: TransactionService
) {
    @PostMapping("/once-donation")
    fun createOnceDonation(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody onceDonationRequest: OnceDonationRequest
    ) = transactionService.createOnceDonation(userDetails, onceDonationRequest)

    @PostMapping("/goal-donation")
    fun createGoalDonation(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody goalDonationRequest: GoalDonationRequest
    ) = transactionService.createGoalDonation(userDetails, goalDonationRequest)

    @GetMapping("/{id}")
    fun getTransaction(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable("id") id: UUID
    ) = transactionService.getTransaction(userDetails, id)
}