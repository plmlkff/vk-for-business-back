package ru.itmo.blpslab1.service;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import ru.itmo.blpslab1.rest.dto.request.AuthRequest;
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest;
import ru.itmo.blpslab1.rest.dto.response.AuthResponse;
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse;

public interface AuthUserService {
    Either<HttpStatus, AuthResponse> auth(AuthRequest authRequest);

    Either<HttpStatus, SignUpResponse> signUp(SignUpRequest signUpRequest);
}
