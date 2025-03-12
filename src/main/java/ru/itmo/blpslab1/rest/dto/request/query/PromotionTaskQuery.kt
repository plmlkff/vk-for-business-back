package ru.itmo.blpslab1.rest.dto.request.query

import jakarta.validation.constraints.Min
import ru.itmo.blpslab1.domain.enums.PromotionType
import java.util.UUID

data class PromotionTaskQuery(
    @field:Min(1)
    val limit: Int = 50,
    @field:Min(0)
    val offset: Int = 0,
    val ids: Set<UUID>?,
    val isApproved: Boolean?,
    val promotionTypes: Set<PromotionType>?
)