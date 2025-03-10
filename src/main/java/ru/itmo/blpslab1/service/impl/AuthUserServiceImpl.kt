package ru.itmo.blpslab1.service.impl;

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.config.JwtProperties
import ru.itmo.blpslab1.domain.enums.UserRole
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.domain.repository.UserRoleRepository
import ru.itmo.blpslab1.rest.dto.request.AuthRequest
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest
import ru.itmo.blpslab1.rest.dto.response.AuthResponse
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse
import ru.itmo.blpslab1.security.entity.JwtUserDetails
import ru.itmo.blpslab1.service.AuthUserService
import ru.itmo.blpslab1.utils.security.JwtUtil
import ru.itmo.blpslab1.utils.security.SHA512HashUtil
import ru.itmo.blpslab1.utils.service.*

@Service
class AuthUserServiceImpl(
    private var jwtProperties: JwtProperties,
    private var userRepository: UserRepository,
    private var userRoleRepository: UserRoleRepository
): AuthUserService {

    @Transactional
    override fun auth(@Valid authRequest: AuthRequest): Result<AuthResponse> {
        val user = userRepository.findUserByLogin(authRequest.login) ?: return error(NOT_FOUND)

        if (!SHA512HashUtil.compare(authRequest.password, user.password)) return error(UNAUTHORIZED)

        val token = JwtUtil.createToken(JwtUserDetails.fromDomain(user), jwtProperties);

        return ok(AuthResponse.fromDomain(user, token))
    }

    @Transactional
    override fun signUp(@Valid signUpRequest: SignUpRequest): Result<SignUpResponse> {
        if(userRepository.existsUserByLogin(signUpRequest.login)) return error(UNAUTHORIZED)

        val passwordHash = SHA512HashUtil.hash(signUpRequest.password)
        val user = SignUpRequest.toDomain(signUpRequest)
        user.password = passwordHash
        val role = userRoleRepository.findUserRoleByName(UserRole.ROLE_USER.roleName) ?: return error(I_AM_A_TEAPOT)
        user.roles = setOf(role)

        userRepository.save(user);

        val userDetails = JwtUserDetails.fromDomain(user)
        val token = JwtUtil.createToken(userDetails, jwtProperties)

        return ok(SignUpResponse.fromDomain(user, token))
    }
}
