package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "donation")
@Entity
@Getter
@Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String target;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Double amount;

    @Column(name = "current_amount", nullable = false)
    @NotNull
    @Min(0)
    private Double currentAmount;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Version
    @Column
    private Integer version;
}
