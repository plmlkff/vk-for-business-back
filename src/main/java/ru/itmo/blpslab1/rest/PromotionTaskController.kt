package ru.itmo.blpslab1.rest

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.blpslab1.rest.dto.request.PromotionTaskRequest
import ru.itmo.blpslab1.rest.dto.request.query.PromotionTaskQuery
import ru.itmo.blpslab1.service.PromotionTaskService
import java.util.UUID

@RestController
@RequestMapping("/api/promotion-task")
@SecurityRequirement(name = "JWT")
class PromotionTaskController(
    private val promotionTaskService: PromotionTaskService
) {

    @PostMapping("/")
    fun createPromotionTask(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody promotionTaskRequest: PromotionTaskRequest
    ) = promotionTaskService.createPromotionTask(userDetails, promotionTaskRequest)

    @GetMapping("/{id}")
    fun getPromotionTask(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = promotionTaskService.getPromotionTask(userDetails, id)

    @PatchMapping("/{id}")
    fun editPromotionTask(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody promotionTaskRequest: PromotionTaskRequest
    ) = promotionTaskService.editPromotionTask(userDetails, promotionTaskRequest.copy(id= id))

    @DeleteMapping("/{id}")
    fun removePromotionTask(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
        ) = promotionTaskService.removePromotionTask(userDetails, id)

    @PatchMapping("/{id}/approve")
    fun createPromotionTask(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = promotionTaskService.approvePromotionTask(userDetails, id)

    @PostMapping("/getAll")
    fun getAll(
        @Valid @RequestBody promotionTaskQuery: PromotionTaskQuery
    ) = promotionTaskService.getAll(promotionTaskQuery)
}