package ru.itmo.blpslab1.domain.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum UserAuthority implements GrantedAuthority {
    VIEW_USER("VIEW_USER"),
    EDIT_USER("EDIT_USER");
    final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
