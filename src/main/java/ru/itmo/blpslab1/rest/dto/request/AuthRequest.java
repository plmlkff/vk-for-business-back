package ru.itmo.blpslab1.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull
    private String login;

    @NotNull
    private String password;
}
