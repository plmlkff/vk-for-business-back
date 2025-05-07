package ru.itmo.blpslab1.rest.dto.request.query

import jakarta.validation.constraints.Min
import ru.itmo.blpslab1.domain.enums.UserRole
import java.util.UUID

data class UserQuery(
    @field:Min(1)
    val limit: Int = 50,
    @field:Min(0)
    val offset: Int = 0,
    val ids: Set<UUID>? = null,
    val roles: Set<UserRole>? = null
)