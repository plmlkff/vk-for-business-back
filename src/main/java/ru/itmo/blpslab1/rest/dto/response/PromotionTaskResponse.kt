package ru.itmo.blpslab1.rest.dto.response

import ru.itmo.blpslab1.domain.entity.PromotionTask
import ru.itmo.blpslab1.domain.enums.PromotionType
import java.util.UUID

data class PromotionTaskResponse(
    val id: UUID,
    val subject: String,
    val body: String,
    val image: ByteArray? = null,
    val isApproved: Boolean,
    val promotionType: PromotionType,
    val group: GroupResponse
)

fun PromotionTask.toResponse() = PromotionTaskResponse(
    id = id,
    subject = subject,
    body = body,
    promotionType = promotionType,
    isApproved = isApproved,
    group = group.toResponse()
)
