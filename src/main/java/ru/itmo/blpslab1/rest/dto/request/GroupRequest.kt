package ru.itmo.blpslab1.rest.dto.request

import ru.itmo.blpslab1.domain.entity.Group
import ru.itmo.blpslab1.domain.entity.User
import java.util.UUID

data class GroupRequest(
    val id: UUID?,
    val name: String,
    val ownerId: UUID,
    val subscriberIds: Set<UUID> = setOf()
)

fun GroupRequest.toDomain() = Group().also {
    it.id = id
    it.name = name
    it.owner = User().apply { id = ownerId }
    it.subscribers = subscriberIds.map { User().apply { id = it } }.toSet()
}