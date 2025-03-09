package ru.itmo.blpslab1.service.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.repository.GoalRepository
import ru.itmo.blpslab1.rest.dto.request.GoalRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.GoalResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.GoalService
import ru.itmo.blpslab1.utils.core.hasAccessTo
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class GoalServiceImpl(
    private val goalRepository: GoalRepository
) : GoalService {

    @Transactional
    override fun createGoal(
        goalRequest: GoalRequest
    ): Either<HttpStatus, GoalResponse> {
        if (goalRequest.id != null) return BAD_REQUEST.left()

        val goal = goalRequest.toDomain()

        return goalRepository.save(goal).toResponse().right()
    }

    @Transactional
    override fun getGoal(
        userDetails: UserDetails, id: UUID
    ): Either<HttpStatus, GoalResponse> {
        val goal = goalRepository.findById(id).getOrNull() ?: return NOT_FOUND.left()

        return if (userDetails hasNoAccessTo goal) METHOD_NOT_ALLOWED.left()
        else goal.toResponse().right()
    }

    @Transactional
    override fun editGoal(
        userDetails: UserDetails, goalRequest: GoalRequest
    ): Either<HttpStatus, GoalResponse> {
        val goalId = goalRequest.id ?: return BAD_REQUEST.left()

        val dbGoal = goalRepository.findById(goalId).getOrNull() ?: return NOT_FOUND.left()

        return if (userDetails hasNoAccessTo dbGoal) METHOD_NOT_ALLOWED.left()
        else goalRepository.save(goalRequest.toDomain()).toResponse().right()
    }

    @Transactional
    override fun removeGoal(
        userDetails: UserDetails, id: UUID
    ): Either<HttpStatus, Unit> {
        val dbGoal = goalRepository.findById(id).getOrNull() ?: return NOT_FOUND.left()

        return if (userDetails hasAccessTo dbGoal) goalRepository.delete(dbGoal).right()
        else METHOD_NOT_ALLOWED.left()
    }
}
