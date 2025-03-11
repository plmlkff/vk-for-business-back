package ru.itmo.blpslab1.rest

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import ru.itmo.blpslab1.rest.dto.request.SubscriptionRequest
import ru.itmo.blpslab1.service.SubscriptionService
import java.util.UUID

@RestController
@RequestMapping("/api/subscription")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) {

    @PostMapping("/")
    fun createSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody subscriptionRequest: SubscriptionRequest
    ) = subscriptionService.createSubscription(userDetails, subscriptionRequest)

    @GetMapping("/{id}")
    fun getSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = subscriptionService.getSubscription(userDetails, id)

    @PatchMapping("/{id}")
    fun editSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody subscriptionRequest: SubscriptionRequest
    ) = subscriptionService.editSubscription(userDetails, subscriptionRequest.copy(id= id))

    @DeleteMapping("/{id}")
    fun removeSubscription(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = subscriptionService.removeSubscription(userDetails, id)
}