package ru.itmo.blpslab1.rest

import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
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

    @PreAuthorize("hasAnyAuthority('GOAL_CREATE', 'GOAL_ADMIN')")
    @PostMapping("/")
    fun createGoal(
        @Valid @RequestBody goalRequest: GoalRequest
    ) = goalService.createGoal(goalRequest).toResponse()

    @PreAuthorize("hasAnyAuthority('GOAL_VIEW', 'GOAL_ADMIN')")
    @GetMapping("/{id}")
    fun readGoal(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = goalService.getGoal(userDetails, id).toResponse()

    @PreAuthorize("hasAnyAuthority('GOAL_EDIT', 'GOAL_ADMIN')")
    @PatchMapping("/{id}")
    fun updateGoal(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody goalRequest: GoalRequest
    ) = goalRequest.copy(id = id)
        .run { goalService.editGoal(userDetails, this) }

    @PreAuthorize("hasAnyAuthority('GOAL_DELETE', 'GOAL_ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteGoal(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = goalService.removeGoal(userDetails, id).toResponse()
}