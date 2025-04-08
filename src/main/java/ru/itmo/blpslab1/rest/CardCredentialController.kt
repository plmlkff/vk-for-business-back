package ru.itmo.blpslab1.rest

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.blpslab1.rest.dto.request.CardCredentialRequest
import ru.itmo.blpslab1.service.CardCredentialService
import java.util.UUID

@RestController
@RequestMapping("/api/card-credentials")
class CardCredentialController(
    val cardCredentialService: CardCredentialService
) {
    @PostMapping("/")
    fun createCardCredential(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody cardCredentialRequest: CardCredentialRequest
    ) = cardCredentialService.createCardCredential(userDetails, cardCredentialRequest)

    @GetMapping("/{id}")
    fun getCardCredential(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = cardCredentialService.getCardCredential(userDetails, id)

    @PatchMapping("/{id}")
    fun editCardCredential(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody cardCredentialRequest: CardCredentialRequest
    ) = cardCredentialRequest.copy(id = id)
        .run { cardCredentialService.editCardCredential(userDetails, this) }

    @DeleteMapping("/{id}")
    fun removeCardCredential(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = cardCredentialService.removeCardCredential(userDetails, id)
}