package ru.itmo.blpslab1.rest.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.blpslab1.domain.entity.User;

import java.util.UUID;

@Data
public class SignUpResponse {
    @NotNull
    private UUID id;

    @NotNull
    private String login;

    @NotNull
    private String token;

    public static SignUpResponse fromDomain(User user, String token){
        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setId(user.getId());
        signUpResponse.setLogin(user.getLogin());
        signUpResponse.setToken(token);

        return signUpResponse;
    }
}
