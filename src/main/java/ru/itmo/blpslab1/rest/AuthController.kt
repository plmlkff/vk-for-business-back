package ru.itmo.blpslab1.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ru.itmo.blpslab1.rest.dto.request.AuthRequest
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest
import ru.itmo.blpslab1.rest.dto.request.query.UserQuery
import ru.itmo.blpslab1.service.AuthUserService
import ru.itmo.blpslab1.utils.core.toResponse

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "JWT")
class AuthController(
    private var authUserService: AuthUserService
) {
    @PostMapping("/auth")
    fun authUser(
        @Valid @RequestBody authRequest: AuthRequest
    ) = authUserService.auth(authRequest).toResponse()

    @PostMapping("/signup")
    fun signUpUser(
        @Valid @RequestBody signUpRequest: SignUpRequest
    ) = authUserService.signUp(signUpRequest).toResponse()

    @PostMapping("/getAll")
    fun getAll(
        @Valid @RequestBody userQuery: UserQuery
    ) = authUserService.getAll(userQuery)
}
