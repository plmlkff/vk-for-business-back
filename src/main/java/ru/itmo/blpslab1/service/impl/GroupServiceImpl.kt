package ru.itmo.blpslab1.service.impl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.entity.Group
import ru.itmo.blpslab1.domain.repository.GroupRepository
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.rest.dto.request.GroupRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.GroupResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.GroupService
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.core.test
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
@SecurityRequirement(name = "JWT")
class GroupServiceImpl(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) : GroupService {

    @Transactional
    override fun createGroup(
        groupRequest: GroupRequest
    ): Either<HttpStatus, GroupResponse>{
        if (groupRequest.id != null) return BAD_REQUEST.left()

        val newGroup = groupRequest.toFilledDomain() ?: return CONFLICT.left()

        return groupRepository.save(newGroup).toResponse().right()
    }

    @Transactional
    override fun getGroup(
        userDetails: UserDetails, id: UUID
    ) = groupRepository.findById(id).getOrNull().test(
        condition = { it != null },
        onTrue = { group ->
            if (userDetails hasNoAccessTo group!!) METHOD_NOT_ALLOWED.left()
            else group.toResponse().right()
        },
        onFalse = { NOT_FOUND.left() }
    )

    @Transactional
    override fun editGroup(
        userDetails: UserDetails,
        groupRequest: GroupRequest
    ): Either<HttpStatus, GroupResponse> {
        if (groupRequest.id == null) return BAD_REQUEST.left()

        val dbGroup = groupRepository.findById(groupRequest.id).getOrNull() ?: return NOT_FOUND.left()

        if (userDetails hasNoAccessTo dbGroup) return METHOD_NOT_ALLOWED.left()

        val newGroup = groupRequest.toFilledDomain() ?: return CONFLICT.left()

        return groupRepository.save(newGroup).toResponse().right()
    }

    @Transactional
    override fun removeGroup(
        userDetails: UserDetails, id: UUID
    ) = groupRepository.findById(id).getOrNull().test(
        condition = { it != null },
        onTrue = { group ->
            if (userDetails hasNoAccessTo group!!) METHOD_NOT_ALLOWED.left()
            else groupRepository.delete(group).right()
        },
        onFalse = { NOT_FOUND.left() }
    )

    private fun GroupRequest.toFilledDomain(): Group?{
        val newOwner = userRepository.findById(ownerId).getOrNull() ?: return null
        val newSubscribers = userRepository.findAllById(subscriberIds)

        return toDomain().also {
            it.owner = newOwner
            it.subscribers = newSubscribers.toSet()
        }
    }
}