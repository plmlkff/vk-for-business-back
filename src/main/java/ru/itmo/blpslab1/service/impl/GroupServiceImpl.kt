package ru.itmo.blpslab1.service.impl

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
import ru.itmo.blpslab1.utils.service.Result
import ru.itmo.blpslab1.utils.service.*
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class GroupServiceImpl(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) : GroupService {

    @Transactional
    override fun createGroup(
        userDetails: UserDetails,
        groupRequest: GroupRequest
    ): Result<GroupResponse> {
        if (groupRequest.id != null) return error(BAD_REQUEST)

        val owner = userRepository.findById(groupRequest.ownerId).getOrNull() ?: return error(NOT_FOUND)

        if (!userDetails.username.equals(owner.login)) return error(METHOD_NOT_ALLOWED)

        val newGroup = groupRequest.toFilledDomain() ?: return error(CONFLICT)

        return ok(groupRepository.save(newGroup).toResponse())
    }

    @Transactional
    override fun getGroup(
        userDetails: UserDetails, id: UUID
    ) = groupRepository.findById(id).getOrNull().test(
        condition = { it != null },
        onTrue = { group ->
            if (userDetails hasNoAccessTo group!!) error(METHOD_NOT_ALLOWED)
            else ok(group.toResponse())
        },
        onFalse = { error(NOT_FOUND) }
    )

    @Transactional
    override fun editGroup(
        userDetails: UserDetails,
        groupRequest: GroupRequest
    ): Result<GroupResponse> {
        if (groupRequest.id == null) return error(BAD_REQUEST)

        val dbGroup = groupRepository.findById(groupRequest.id).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo dbGroup) return error(METHOD_NOT_ALLOWED)

        val newGroup = groupRequest.toFilledDomain() ?: return error(CONFLICT)

        return ok(groupRepository.save(newGroup).toResponse())
    }

    @Transactional
    override fun removeGroup(
        userDetails: UserDetails, id: UUID
    ) = groupRepository.findById(id).getOrNull().test(
        condition = { it != null },
        onTrue = { group ->
            if (userDetails hasNoAccessTo group!!) error(METHOD_NOT_ALLOWED)
            else ok(groupRepository.delete(group))
        },
        onFalse = { error(NOT_FOUND) }
    )

    override fun getAllByUser(
        userDetails: UserDetails
    ): Result<List<GroupResponse>> = ok(groupRepository.findAllByOwner(userDetails.username).map(Group::toResponse))

    private fun GroupRequest.toFilledDomain(): Group?{
        val newOwner = userRepository.findById(ownerId).getOrNull() ?: return null
        val newSubscribers = userRepository.findAllById(subscriberIds)

        return toDomain().also {
            it.owner = newOwner
            it.subscribers = newSubscribers.toSet()
        }
    }
}