package ru.itmo.blpslab1.rest.dto.request

import ru.itmo.blpslab1.domain.entity.PromotionTask
import ru.itmo.blpslab1.domain.enums.PromotionType
import java.util.UUID

data class PromotionTaskRequest(
    val id: UUID?,
    val subject: String,
    val body: String,
    val image: ImageRequest?,
    val promotionType: PromotionType,
    val groupId: UUID
)

fun PromotionTaskRequest.toDomain() = PromotionTask().also {
    it.id = id
    it.subject = subject
    it.body = body
    it.promotionType = promotionType
}
