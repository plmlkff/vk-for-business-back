package ru.itmo.blpslab1.service

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.GoalRequest
import ru.itmo.blpslab1.rest.dto.response.GoalResponse
import java.util.UUID

interface GoalService {
    @PreAuthorize("hasAnyAuthority('GOAL_CREATE', 'GOAL_ADMIN')")
    fun createGoal(goalRequest: GoalRequest): Either<HttpStatus, GoalResponse>

    @PreAuthorize("hasAnyAuthority('GOAL_VIEW', 'GOAL_ADMIN')")
    fun getGoal(userDetails: UserDetails, id: UUID): Either<HttpStatus, GoalResponse>

    @PreAuthorize("hasAnyAuthority('GOAL_EDIT', 'GOAL_ADMIN')")
    fun editGoal(userDetails: UserDetails, goalRequest: GoalRequest): Either<HttpStatus, GoalResponse>

    @PreAuthorize("hasAnyAuthority('GOAL_DELETE', 'GOAL_ADMIN')")
    fun removeGoal(userDetails: UserDetails, id: UUID): Either<HttpStatus, Unit>
}
