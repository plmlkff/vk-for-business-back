package ru.itmo.blpslab1.rest.dto.response

import org.hibernate.validator.constraints.CreditCardNumber
import ru.itmo.blpslab1.domain.entity.CardCredential
import java.util.Date
import java.util.UUID

data class CardCredentialResponse(
    val id: UUID,
    @CreditCardNumber
    val cardNumber: String,
    val endDate: Date,
    val cvv: Short,
    val owner: UserResponse
)

fun CardCredential.toResponse() = CardCredentialResponse(
    id = id,
    cardNumber = cardNumber,
    endDate = endDate,
    cvv = cvv,
    owner = owner.toResponse()
)
