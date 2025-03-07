package ru.itmo.blpslab1.utils.core

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun Either<HttpStatus, *>.toResponse() = fold(
    ifLeft = {left -> ResponseEntity.status(left).build()},
    ifRight = {right -> ResponseEntity.ok(right)}
)