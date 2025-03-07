package ru.itmo.blpslab1.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.blpslab1.rest.dto.request.AuthRequest;
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest;
import ru.itmo.blpslab1.rest.dto.response.AuthResponse;
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse;
import ru.itmo.blpslab1.service.AuthUserService;

import java.util.function.Function;

@RestController("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private AuthUserService authUserService;

    @PostMapping("/auth")
    public AuthResponse authUser(@Valid @RequestBody AuthRequest authRequest){
        return authUserService.auth(authRequest).fold(
                left -> new AuthResponse(),
                right -> right
        );
    }

    @PostMapping("/signup")
    public SignUpResponse signUpUser(@Valid @RequestBody SignUpRequest signUpRequest){
        return null;
    }
}
