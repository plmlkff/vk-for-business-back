package ru.itmo.blpslab1.service.impl;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.itmo.blpslab1.config.JwtProperties;
import ru.itmo.blpslab1.domain.entity.User;
import ru.itmo.blpslab1.domain.repository.UserRepository;
import ru.itmo.blpslab1.rest.dto.request.AuthRequest;
import ru.itmo.blpslab1.rest.dto.request.SignUpRequest;
import ru.itmo.blpslab1.rest.dto.response.AuthResponse;
import ru.itmo.blpslab1.rest.dto.response.SignUpResponse;
import ru.itmo.blpslab1.security.entity.JwtUserDetails;
import ru.itmo.blpslab1.service.AuthUserService;
import ru.itmo.blpslab1.utils.security.JwtUtil;
import ru.itmo.blpslab1.utils.security.SHA512HashUtil;

import java.util.Optional;

import static io.vavr.control.Either.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private JwtProperties jwtProperties;

    private UserRepository userRepository;

    @Override
    public Either<HttpStatus, AuthResponse> auth(AuthRequest authRequest) {
        Optional<User> userRes = userRepository.findUserByLogin(authRequest.getLogin());
        if (userRes.isEmpty()) return left(NOT_FOUND);
        User user = userRes.get();

        if (!SHA512HashUtil.check(authRequest.getPassword(), user.getPassword())){
            return left(UNAUTHORIZED);
        }

        String token = JwtUtil.createToken(JwtUserDetails.fromDomain(user), jwtProperties);

        return right(AuthResponse.fromDomain(user, token));
    }

    @Override
    public Either<HttpStatus, SignUpResponse> signUp(SignUpRequest signUpRequest) {
        Optional<User> userRes = userRepository.findUserByLogin(signUpRequest.getLogin());
        if (userRes.isPresent()) return left(UNAUTHORIZED);

        String passwordHash = SHA512HashUtil.hash(signUpRequest.getPassword());
        User newUser = SignUpRequest.toDomain(signUpRequest);
        newUser.setPassword(passwordHash);

        userRepository.save(newUser);

        String token = JwtUtil.createToken(JwtUserDetails.fromDomain(newUser), jwtProperties);

        return right(SignUpResponse.fromDomain(newUser, token));
    }
}
