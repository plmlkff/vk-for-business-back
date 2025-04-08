package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.SubscriptionRequest
import ru.itmo.blpslab1.rest.dto.response.SubscriptionResponse
import ru.itmo.blpslab1.utils.service.Result;
import java.util.UUID

interface SubscriptionService {

    @PreAuthorize("hasAnyAuthority('SUBSCRIPTION_CREATE', 'SUBSCRIPTION_ADMIN')")
    fun createSubscription(
        userDetails: UserDetails,
        subscriptionRequest: SubscriptionRequest
    ): Result<SubscriptionResponse>

    @PreAuthorize("hasAnyAuthority('SUBSCRIPTION_VIEW', 'SUBSCRIPTION_ADMIN')")
    fun getSubscription(
        userDetails: UserDetails,
        id: UUID
    ): Result<SubscriptionResponse>

    @PreAuthorize("hasAnyAuthority('SUBSCRIPTION_ADMIN')")
    fun editSubscription(
        userDetails: UserDetails,
        subscriptionRequest: SubscriptionRequest
    ): Result<SubscriptionResponse>

    @PreAuthorize("hasAnyAuthority('SUBSCRIPTION_DELETE', 'SUBSCRIPTION_ADMIN')")
    fun removeSubscription(
        userDetails: UserDetails,
        id: UUID
    ): Result<Unit>

    @PreAuthorize("hasAnyAuthority('SUBSCRIPTION_VIEW', 'SUBSCRIPTION_ADMIN')")
    fun getAllByOwner(
        userDetails: UserDetails
    ): Result<List<SubscriptionResponse>>

    @PreAuthorize("hasAnyAuthority('SUBSCRIPTION_ADMIN')")
    fun markSubscriptionPaid(
        userDetails: UserDetails,
        id: UUID
    ): Result<SubscriptionResponse>
}