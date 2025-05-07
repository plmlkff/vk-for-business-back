package ru.itmo.blpslab1.rest.dto.response

import ru.itmo.blpslab1.domain.entity.Tariff
import java.util.UUID

data class TariffResponse(
    val id: UUID,
    val name: String,
    val price: Double,
    var previewImage: ByteArray?,
    val group: GroupResponse
)

fun Tariff.toResponse() = TariffResponse(
    id = id,
    name = name,
    price = price,
    previewImage = null,
    group = group.toResponse()
)
