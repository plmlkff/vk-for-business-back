package ru.itmo.blpslab1.rest;

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.blpslab1.rest.dto.request.AuthRequest
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest
import ru.itmo.blpslab1.service.AuthUserService
import ru.itmo.blpslab1.utils.core.toResponse

@RestController
@RequestMapping("/api/users")
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
}
