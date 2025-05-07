package ru.itmo.blpslab1.rest.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.blpslab1.domain.entity.User;

import java.util.UUID;

@Data
public class AuthResponse {
    @NotNull
    private UUID id;

    @NotNull
    private String login;

    @NotNull
    private String token;

    public static AuthResponse fromDomain(User user, String token){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setId(user.getId());
        authResponse.setLogin(user.getLogin());
        authResponse.setToken(token);
        return authResponse;
    }
}
