package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.blpslab1.domain.enums.UserAuthority;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_role")
@Getter
@Setter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @ElementCollection(targetClass = UserAuthority.class)
    @CollectionTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "role_id")
    )
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Set<UserAuthority> authorities;
}
