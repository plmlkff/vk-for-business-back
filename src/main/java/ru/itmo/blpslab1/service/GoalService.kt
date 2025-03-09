package ru.itmo.blpslab1.service

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.GoalRequest
import ru.itmo.blpslab1.rest.dto.response.GoalResponse
import java.util.UUID

interface GoalService {
    fun createGoal(goalRequest: GoalRequest): Either<HttpStatus, GoalResponse>

    fun getGoal(userDetails: UserDetails, id: UUID): Either<HttpStatus, GoalResponse>

    fun editGoal(userDetails: UserDetails, goalRequest: GoalRequest): Either<HttpStatus, GoalResponse>

    fun removeGoal(userDetails: UserDetails, id: UUID): Either<HttpStatus, Unit>
}
