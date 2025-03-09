package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "goal")
@Entity
@Getter
@Setter
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    @Size(min = 1)
    private String name;

    @Column(name = "target_sum", nullable = false)
    @NotNull
    @Min(0)
    private Double targetSum;

    @Column(name = "current_sum", nullable = false)
    @NotNull
    @Min(0)
    private Double currentSum;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    @Version
    private Integer version;
}
