package ru.itmo.blpslab1.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull
    @Size(min = 3)
    private String login;

    @NotNull
    @Size(min = 3)
    private String password;
}
