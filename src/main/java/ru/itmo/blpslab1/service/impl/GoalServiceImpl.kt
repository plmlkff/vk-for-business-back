package ru.itmo.blpslab1.service.impl

import org.hibernate.StaleStateException
import org.springframework.http.HttpStatus.*
import org.springframework.retry.annotation.Retryable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.enums.TransactionType
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.domain.repository.CardCredentialRepository
import ru.itmo.blpslab1.domain.repository.GoalRepository
import ru.itmo.blpslab1.domain.repository.GroupRepository
import ru.itmo.blpslab1.rest.dto.request.GoalAmountChangeRequest
import ru.itmo.blpslab1.rest.dto.request.GoalRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.GoalResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.GoalService
import ru.itmo.blpslab1.utils.core.hasAccessTo
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.core.hasNoAuthority
import ru.itmo.blpslab1.utils.service.*
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class GoalServiceImpl(
    private val goalRepository: GoalRepository,
    private val groupRepository: GroupRepository,
    private val cardCredentialRepository: CardCredentialRepository
) : GoalService {

    @Transactional
    override fun createGoal(
        goalRequest: GoalRequest
    ): Result<GoalResponse> {
        if (goalRequest.id != null) return error(BAD_REQUEST)

        val dbGroup = groupRepository.findById(goalRequest.groupId).getOrNull() ?: return error(NOT_FOUND)
        val dbCardCredential = cardCredentialRepository.findById(goalRequest.recipientCardId).getOrNull() ?: return error(NOT_FOUND)

        val goal = goalRequest.toDomain().apply {
            group = dbGroup
            recipientCard = dbCardCredential
        }

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
        val dbCardCredential =
            cardCredentialRepository.findById(goalRequest.recipientCardId).getOrNull() ?: return error(NOT_FOUND)

        return if (userDetails hasNoAccessTo dbGoal || userDetails hasNoAccessTo dbCardCredential) error(
            METHOD_NOT_ALLOWED
        )
        else ok(goalRepository.save(goalRequest.toDomain()
            .apply {
                recipientCard = dbCardCredential
            }).toResponse()
        )
    }

    @Transactional
    override fun removeGoal(
        userDetails: UserDetails, id: UUID
    ): Result<Unit> {
        val dbGoal = goalRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        return if (userDetails hasAccessTo dbGoal) ok(goalRepository.delete(dbGoal))
        else error(METHOD_NOT_ALLOWED)
    }

    @Retryable(maxAttempts = 20, retryFor = [StaleStateException::class])
    @Transactional
    override fun editGoalAmount(userDetails: UserDetails, request: GoalAmountChangeRequest): Result<GoalResponse> {
        if (userDetails hasNoAuthority UserAuthority.GOAL_ADMIN) return error(METHOD_NOT_ALLOWED)

        val dbGoal = goalRepository.findById(request.goalId).getOrNull() ?: return error(NOT_FOUND)

        when(request.transactionType){
            TransactionType.DEBIT -> dbGoal.currentSum += request.amount
            TransactionType.WITHDRAW -> dbGoal.currentSum -= request.amount
        }

        if (dbGoal.currentSum < 0) return error()

        return ok(goalRepository.save(dbGoal).toResponse())
    }
}
