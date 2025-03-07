package ru.itmo.blpslab1.security.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.blpslab1.domain.entity.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class JwtUserDetails implements UserDetails {
    private String userName;

    private Set<GrantedAuthority> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public static JwtUserDetails fromDomain(User user) {
        JwtUserDetails userDetails = new JwtUserDetails();
        userDetails.setUserName(user.getLogin());
        userDetails.getRoles().addAll(
                user.getRoles().stream()
                .flatMap(userRole -> userRole.getAuthorities().stream())
                .collect(Collectors.toSet())
        );
        return userDetails;
    }
}
