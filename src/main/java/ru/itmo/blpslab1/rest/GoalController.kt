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
import ru.itmo.blpslab1.rest.dto.request.GoalRequest
import ru.itmo.blpslab1.service.GoalService
import ru.itmo.blpslab1.utils.core.toResponse
import java.util.UUID

@RestController
@RequestMapping("/api/goals")
class GoalController(
    private val goalService: GoalService
) {

    @PostMapping("/")
    fun createGoal(
        @Valid @RequestBody goalRequest: GoalRequest
    ) = goalService.createGoal(goalRequest).toResponse()

    @GetMapping("/{id}")
    fun getGoal(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = goalService.getGoal(userDetails, id).toResponse()

    @PatchMapping("/{id}")
    fun editGoal(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody goalRequest: GoalRequest
    ) = goalRequest.copy(id = id)
        .run { goalService.editGoal(userDetails, this) }

    @DeleteMapping("/{id}")
    fun removeGoal(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = goalService.removeGoal(userDetails, id).toResponse()

    @GetMapping("/by-group/{groupId}")
    fun getAllByGroupId(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable("groupId") groupId: UUID
    ) = goalService.getAllByGroupId(userDetails, groupId)
}