package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.CardCredentialRequest
import ru.itmo.blpslab1.rest.dto.response.CardCredentialResponse
import java.util.UUID
import ru.itmo.blpslab1.utils.service.*

interface CardCredentialService {
    @PreAuthorize("hasAnyAuthority('CARD_CREDENTIAL_CREATE', 'CARD_CREDENTIAL_ADMIN')")
    fun createCardCredential(userDetails: UserDetails, cardCredentialRequest: CardCredentialRequest): Result<CardCredentialResponse>

    @PreAuthorize("hasAnyAuthority('CARD_CREDENTIAL_VIEW', 'CARD_CREDENTIAL_ADMIN')")
    fun getCardCredential(userDetails: UserDetails, id: UUID): Result<CardCredentialResponse>

    @PreAuthorize("hasAnyAuthority('CARD_CREDENTIAL_EDIT', 'CARD_CREDENTIAL_ADMIN')")
    fun editCardCredential(userDetails: UserDetails, cardCredentialRequest: CardCredentialRequest): Result<CardCredentialResponse>

    @PreAuthorize("hasAnyAuthority('CARD_CREDENTIAL_DELETE', 'CARD_CREDENTIAL_ADMIN')")
    fun removeCardCredential(userDetails: UserDetails, id: UUID): Result<Unit>

    @PreAuthorize("hasAnyAuthority('CARD_CREDENTIAL_VIEW', 'CARD_CREDENTIAL_ADMIN')")
    fun getAllCardCredentialByUser(userDetails: UserDetails): Result<List<CardCredentialResponse>>
}