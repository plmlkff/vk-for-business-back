package ru.itmo.blpslab1.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table(name = "community_group")
@Entity
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_to_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> subscribers;

    @OneToMany(mappedBy = PromotionTask.Fields.GROUP, cascade = CascadeType.ALL)
    private Set<PromotionTask> promotionTasks;

    @OneToMany(mappedBy = Goal.Fields.GROUP, cascade = CascadeType.ALL)
    private Set<Goal> goals;

    public interface Fields{
        String OWNER = "owner";
    }
}
