package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.PromotionTaskRequest
import ru.itmo.blpslab1.rest.dto.response.PromotionTaskResponse
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID

interface PromotionTaskService {

    @PreAuthorize("hasAnyAuthority('PROMOTION_TASK_CREATE', 'PROMOTION_TASK_ADMIN')")
    fun createPromotionTask(
        userDetails: UserDetails,
        promotionTaskRequest: PromotionTaskRequest
    ): Result<PromotionTaskResponse>

    @PreAuthorize("hasAnyAuthority('PROMOTION_TASK_VIEW', 'PROMOTION_TASK_ADMIN')")
    fun getPromotionTask(
        userDetails: UserDetails,
        id: UUID
    ): Result<PromotionTaskResponse>

    @PreAuthorize("hasAnyAuthority('PROMOTION_TASK_EDIT', 'PROMOTION_TASK_ADMIN')")
    fun editPromotionTask(
        userDetails: UserDetails,
        promotionTaskRequest: PromotionTaskRequest
    ): Result<PromotionTaskResponse>

    @PreAuthorize("hasAnyAuthority('PROMOTION_TASK_DELETE', 'PROMOTION_TASK_ADMIN')")
    fun removePromotionTask(
        userDetails: UserDetails,
        id: UUID
    ): Result<Unit>
}