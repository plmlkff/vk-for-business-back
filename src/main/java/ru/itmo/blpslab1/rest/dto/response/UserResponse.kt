package ru.itmo.blpslab1.rest.dto.response

import ru.itmo.blpslab1.domain.entity.User
import java.util.UUID

data class UserResponse (
    val id: UUID,
    val firstName: String,
    val surname: String,
)

fun User.toResponse(): UserResponse = UserResponse(
    id = id,
    firstName = firstName,
    surname = surname
)