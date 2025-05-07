package ru.itmo.blpslab1.rest.dto.response

import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.CreditCardNumber
import ru.itmo.blpslab1.domain.entity.CardCredential
import java.util.Date
import java.util.UUID

data class CardCredentialResponse(
    val id: UUID,
    @CreditCardNumber
    val cardNumber: String,
    val endDate: Date,
    @field:Size(min = 3)
    @field:Size(max = 3)
    val cvv: String,
    val owner: UserResponse
)

fun CardCredential.toResponse() = CardCredentialResponse(
    id = id,
    cardNumber = cardNumber,
    endDate = endDate,
    cvv = cvv,
    owner = owner.toResponse()
)
