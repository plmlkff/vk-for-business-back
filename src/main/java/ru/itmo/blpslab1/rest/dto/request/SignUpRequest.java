package ru.itmo.blpslab1.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.itmo.blpslab1.domain.entity.User;

@Data
public class SignUpRequest {
    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String login;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String surname;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String password;

    public static User toDomain(SignUpRequest signUpRequest){
        User user = new User();
        user.setLogin(signUpRequest.getLogin());
        user.setFirstName(signUpRequest.getFirstName());
        user.setSurname(signUpRequest.getSurname());

        return user;
    }
}
