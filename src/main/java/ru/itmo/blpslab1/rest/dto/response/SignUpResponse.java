package ru.itmo.blpslab1.rest.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.itmo.blpslab1.domain.entity.User;

@Data
public class SignUpResponse {
    @NotNull
    private String login;

    @NotNull
    private String token;

    public static SignUpResponse fromDomain(User user, String token){
        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setLogin(user.getLogin());
        signUpResponse.setToken(token);

        return signUpResponse;
    }
}
