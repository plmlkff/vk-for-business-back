package ru.itmo.blpslab1.rest.dto.response

import ru.itmo.blpslab1.domain.entity.Group
import java.util.UUID

data class GroupResponse(
    val id: UUID,
    val name: String,
    val owner: UserResponse,
    val subscribers: List<UserResponse>
)

fun Group.toResponse(): GroupResponse = GroupResponse(
    id = id,
    name = name,
    owner = owner.toResponse(),
    subscribers = subscribers.map { it.toResponse() }
)