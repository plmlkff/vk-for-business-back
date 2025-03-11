package ru.itmo.blpslab1.utils.core

import org.springframework.http.ResponseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.domain.entity.CardCredential
import ru.itmo.blpslab1.domain.entity.Goal
import ru.itmo.blpslab1.domain.entity.Group
import ru.itmo.blpslab1.domain.enums.UserAuthority
import ru.itmo.blpslab1.utils.service.Result
import java.util.*

fun Result<*>.toResponse() = fold(
    ifError = {error -> ResponseEntity.status(error).build()},
    ifOk = {body -> ResponseEntity.ok(body)}
)

private fun UserDetails.hasAccessTo(
    entityOwnerLogin: String, vararg adminRoles: GrantedAuthority
) = Objects.equals(entityOwnerLogin, username)
    .or(adminRoles.any { it in authorities })

infix fun UserDetails.hasNoAccessTo(
    goal: Goal
) = !(this hasAccessTo goal)

infix fun UserDetails.hasAccessTo(
    goal: Goal
) = hasAccessTo(goal.group.owner.login, UserAuthority.GOAL_ADMIN)

infix fun UserDetails.hasAccessTo(
    group: Group
) = hasAccessTo(group.owner.login, UserAuthority.GOAL_ADMIN)

infix fun UserDetails.hasNoAccessTo(group: Group) = !(hasAccessTo(group))

infix fun UserDetails.hasAccessTo(
    cardCredential: CardCredential
) = hasAccessTo(cardCredential.owner.login, UserAuthority.CARD_CREDENTIAL_ADMIN)

infix fun UserDetails.hasNoAccessTo(
    cardCredential: CardCredential
) = !(this hasAccessTo cardCredential)

inline fun <T, U> T.test(
    condition: (T) -> Boolean,
    onTrue: (T) -> U,
    onFalse: (T) -> U
) = if (condition(this)) onTrue(this) else onFalse(this)