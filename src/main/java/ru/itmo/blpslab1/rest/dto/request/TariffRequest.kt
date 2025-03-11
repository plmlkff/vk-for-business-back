package ru.itmo.blpslab1.rest.dto.request

import ru.itmo.blpslab1.domain.entity.Tariff
import java.util.UUID

data class TariffRequest(
    val id: UUID?,
    val name: String,
    val price: Double,
    val previewImage: ImageRequest?,
    val groupId: UUID
)

fun TariffRequest.toDomain() = Tariff().also {
    it.id = id
    it.name = name
    it.price = price
}
