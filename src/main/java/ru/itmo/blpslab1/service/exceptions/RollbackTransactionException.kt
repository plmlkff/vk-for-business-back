package ru.itmo.blpslab1.service.exceptions

import org.springframework.http.HttpStatus

class RollbackTransactionException(
    val status: HttpStatus
): RuntimeException()