package ru.itmo.blpslab1.service.impl;

import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.config.JwtProperties
import ru.itmo.blpslab1.domain.entity.User
import ru.itmo.blpslab1.domain.enums.UserRole
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.domain.repository.UserRoleRepository
import ru.itmo.blpslab1.domain.repository.specification.UserSpecification
import ru.itmo.blpslab1.rest.dto.request.AuthRequest
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest
import ru.itmo.blpslab1.rest.dto.request.query.UserQuery
import ru.itmo.blpslab1.rest.dto.response.AuthResponse
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse
import ru.itmo.blpslab1.rest.dto.response.UserResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
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
        val role = userRoleRepository.findUserRoleByName(UserRole.ROLE_USER.name) ?: return error(I_AM_A_TEAPOT)
        user.roles = setOf(role)

        userRepository.save(user);

        val userDetails = JwtUserDetails.fromDomain(user)
        val token = JwtUtil.createToken(userDetails, jwtProperties)

        return ok(SignUpResponse.fromDomain(user, token))
    }

    override fun getAll(userQuery: UserQuery): Result<List<UserResponse>> {
        val pageable = PageRequest.of(userQuery.offset, userQuery.limit)
        val spec = UserSpecification(userQuery)

        return ok(userRepository.findAll(spec, pageable).map { it.toResponse() }.toList())
    }
}
