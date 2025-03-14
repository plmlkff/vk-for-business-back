package ru.itmo.blpslab1.service;

import org.springframework.security.access.prepost.PreAuthorize
import ru.itmo.blpslab1.rest.dto.request.AuthRequest;
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest;
import ru.itmo.blpslab1.rest.dto.request.query.UserQuery
import ru.itmo.blpslab1.rest.dto.response.AuthResponse;
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse;
import ru.itmo.blpslab1.rest.dto.response.UserResponse
import ru.itmo.blpslab1.utils.service.Result

interface AuthUserService {
    fun auth(authRequest: AuthRequest): Result<AuthResponse>

    fun signUp(signUpRequest: SignUpRequest): Result<SignUpResponse>

    @PreAuthorize("hasAuthority('USER_ADMIN')")
    fun getAll(userQuery: UserQuery): Result<List<UserResponse>>
}
