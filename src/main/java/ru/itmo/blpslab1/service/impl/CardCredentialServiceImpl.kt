package ru.itmo.blpslab1.service.impl

import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.domain.repository.CardCredentialRepository
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.rest.dto.request.CardCredentialRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.CardCredentialResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.CardCredentialService
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.core.hasNoAuthority
import ru.itmo.blpslab1.utils.core.test
import java.util.UUID
import ru.itmo.blpslab1.utils.service.*
import kotlin.jvm.optionals.getOrNull

@Service
class CardCredentialServiceImpl(
    val cardCredentialRepository: CardCredentialRepository,
    val userRepository: UserRepository
): CardCredentialService {
    override fun createCardCredential(
        userDetails: UserDetails,
        cardCredentialRequest: CardCredentialRequest
    ): Result<CardCredentialResponse>{
        if (cardCredentialRequest.id != null) return error()

        val owner = userRepository.findById(cardCredentialRequest.ownerId).getOrNull() ?: return error(HttpStatus.NOT_FOUND)

        if (!userDetails.username.equals(owner.login)
            && userDetails hasNoAuthority UserAuthority.CARD_CREDENTIAL_ADMIN) return error(HttpStatus.METHOD_NOT_ALLOWED)

        val newCardCredential = cardCredentialRequest.toDomain().also { it.owner = owner }

        return ok(cardCredentialRepository.save(newCardCredential).toResponse())
    }

    override fun getCardCredential(
        userDetails: UserDetails, id: UUID
    ) = cardCredentialRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = {
            if (userDetails hasNoAccessTo it!!) error(HttpStatus.METHOD_NOT_ALLOWED)
            else ok(it.toResponse())
        },
        onFalse = { error(HttpStatus.NOT_FOUND)}
    )

    override fun editCardCredential(
        userDetails: UserDetails,
        cardCredentialRequest: CardCredentialRequest
    ): Result<CardCredentialResponse>{
        if (cardCredentialRequest.id == null) return error()

        val dbCardCredential = cardCredentialRepository.findById(cardCredentialRequest.id).getOrNull() ?: return error(HttpStatus.NOT_FOUND)

        if (userDetails hasNoAccessTo dbCardCredential) return error(HttpStatus.METHOD_NOT_ALLOWED)

        if (!dbCardCredential.owner.login.equals(userDetails.username)
            && userDetails hasNoAuthority UserAuthority.CARD_CREDENTIAL_ADMIN) return error(HttpStatus.METHOD_NOT_ALLOWED)

        val newCardCredential = cardCredentialRequest.toFilledDomain() ?: return error(HttpStatus.NOT_FOUND)

        return ok(cardCredentialRepository.save(newCardCredential).toResponse())
    }

    override fun removeCardCredential(
        userDetails: UserDetails,
        id: UUID
    ) = cardCredentialRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = {
            if (userDetails hasNoAccessTo it!!) error(HttpStatus.METHOD_NOT_ALLOWED)
            else ok(cardCredentialRepository.delete(it))
        },
        onFalse = { error(HttpStatus.NOT_FOUND)}
    )

    fun CardCredentialRequest.toFilledDomain() = toDomain().let {
        it.owner = userRepository.findById(ownerId).getOrNull() ?: return@let null
        it
    }
}