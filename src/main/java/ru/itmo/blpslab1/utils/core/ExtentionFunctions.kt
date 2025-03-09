package ru.itmo.blpslab1.utils.core

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.domain.entity.Goal
import ru.itmo.blpslab1.domain.enums.UserAuthority
import java.util.*

fun Either<HttpStatus, *>.toResponse() = fold(
    ifLeft = {left -> ResponseEntity.status(left).build()},
    ifRight = {right -> ResponseEntity.ok(right)}
)

private fun UserDetails.hasAccessTo(
    entityOwnerLogin: String, vararg adminRoles: GrantedAuthority
) = Objects.equals(entityOwnerLogin, username)
    .or(adminRoles.any { it in authorities })

infix fun UserDetails.hasNoAccessTo(goal: Goal): Boolean {
    return !(this hasAccessTo goal)
}

infix fun UserDetails.hasAccessTo(goal: Goal): Boolean {
    return hasAccessTo(goal.group.owner.login, UserAuthority.GOAL_ADMIN)
}