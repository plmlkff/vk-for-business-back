package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "person")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String surname;

    @Column(nullable = false)
    @NotNull
    private String login;

    @Column(nullable = false)
    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles;

    @OneToMany(mappedBy = CardCredential.Fields.OWNER, cascade = CascadeType.ALL)
    private Set<CardCredential> credentials;

    @OneToMany(mappedBy = Group.Fields.OWNER, cascade = CascadeType.ALL)
    private Set<Group> groups;

    @OneToMany(mappedBy = Subscription.Fields.OWNER, cascade = CascadeType.ALL)
    private Set<Subscription> subscriptions;

    public interface Fields{
        String ID = "id";
        String ROLES = "roles";
    }
}
