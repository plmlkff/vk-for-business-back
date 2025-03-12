package ru.itmo.blpslab1.rest.dto.request.query

import ru.itmo.blpslab1.domain.enums.PromotionType
import java.util.UUID

data class PromotionTaskQuery(
    val limit: Int = 50,
    val offset: Int = 0,
    val ids: Set<UUID>?,
    val isApproved: Boolean?,
    val promotionTypes: Set<PromotionType>?
)