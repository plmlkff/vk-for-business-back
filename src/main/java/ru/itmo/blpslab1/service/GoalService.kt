package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.domain.entity.Goal
import ru.itmo.blpslab1.rest.dto.request.GoalAmountChangeRequest
import ru.itmo.blpslab1.rest.dto.request.GoalRequest
import ru.itmo.blpslab1.rest.dto.response.GoalResponse
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID

interface GoalService {
    @PreAuthorize("hasAnyAuthority('GOAL_CREATE', 'GOAL_ADMIN')")
    fun createGoal(goalRequest: GoalRequest): Result<GoalResponse>

    @PreAuthorize("hasAnyAuthority('GOAL_VIEW', 'GOAL_ADMIN')")
    fun getGoal(userDetails: UserDetails, id: UUID): Result<GoalResponse>

    @PreAuthorize("hasAnyAuthority('GOAL_EDIT', 'GOAL_ADMIN')")
    fun editGoal(userDetails: UserDetails, goalRequest: GoalRequest): Result<GoalResponse>

    @PreAuthorize("hasAnyAuthority('GOAL_DELETE', 'GOAL_ADMIN')")
    fun removeGoal(userDetails: UserDetails, id: UUID): Result<Unit>

    @PreAuthorize("hasAnyAuthority('GOAL_VIEW', 'GOAL_ADMIN')")
    fun getAllByGroupId(userDetails: UserDetails, groupId: UUID): Result<List<GoalResponse>>

    fun editGoalAmount(userDetails: UserDetails, request: GoalAmountChangeRequest): Result<GoalResponse>
}
