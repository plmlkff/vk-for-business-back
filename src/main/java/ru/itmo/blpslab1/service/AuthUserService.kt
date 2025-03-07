package ru.itmo.blpslab1.service;

import arrow.core.Either
import org.springframework.http.HttpStatus;
import ru.itmo.blpslab1.rest.dto.request.AuthRequest;
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest;
import ru.itmo.blpslab1.rest.dto.response.AuthResponse;
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse;

interface AuthUserService {
    fun auth(authRequest: AuthRequest): Either<HttpStatus, AuthResponse>

    fun signUp(signUpRequest: SignUpRequest): Either<HttpStatus, SignUpResponse>
}
