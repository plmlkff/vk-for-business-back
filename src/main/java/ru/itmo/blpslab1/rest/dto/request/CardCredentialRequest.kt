package ru.itmo.blpslab1.rest.dto.request

import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.CreditCardNumber
import ru.itmo.blpslab1.domain.entity.CardCredential
import java.util.Date
import java.util.UUID

data class CardCredentialRequest(
    val id: UUID?,
    @field:CreditCardNumber
    val cardNumber: String,
    val endDate: Date,
    @field:Size(min = 3)
    @field:Size(max = 3)
    val cvv: String,
    val ownerId: UUID
)

fun CardCredentialRequest.toDomain() = CardCredential().also {
    it.id = id
    it.cardNumber = cardNumber
    it.endDate = endDate
    it.cvv = cvv
}