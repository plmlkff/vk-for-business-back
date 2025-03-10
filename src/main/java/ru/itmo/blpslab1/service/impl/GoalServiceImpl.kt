package ru.itmo.blpslab1.service.impl

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
import ru.itmo.blpslab1.utils.service.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class GoalServiceImpl(
    private val goalRepository: GoalRepository
) : GoalService {

    @Transactional
    override fun createGoal(
        goalRequest: GoalRequest
    ): Result<GoalResponse> {
        if (goalRequest.id != null) return error(BAD_REQUEST)

        val goal = goalRequest.toDomain()

        return ok(goalRepository.save(goal).toResponse())
    }

    @Transactional
    override fun getGoal(
        userDetails: UserDetails, id: UUID
    ): Result<GoalResponse> {
        val goal = goalRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        return if (userDetails hasNoAccessTo goal) error(METHOD_NOT_ALLOWED)
        else ok(goal.toResponse())
    }

    @Transactional
    override fun editGoal(
        userDetails: UserDetails, goalRequest: GoalRequest
    ): Result<GoalResponse> {
        val goalId = goalRequest.id ?: return error(BAD_REQUEST)

        val dbGoal = goalRepository.findById(goalId).getOrNull() ?: return error(NOT_FOUND)

        return if (userDetails hasNoAccessTo dbGoal) error(METHOD_NOT_ALLOWED)
        else ok(goalRepository.save(goalRequest.toDomain()).toResponse())
    }

    @Transactional
    override fun removeGoal(
        userDetails: UserDetails, id: UUID
    ): Result<Unit> {
        val dbGoal = goalRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        return if (userDetails hasAccessTo dbGoal) ok(goalRepository.delete(dbGoal))
        else error(METHOD_NOT_ALLOWED)
    }
}
