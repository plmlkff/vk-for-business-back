package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "subscription")
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_start", nullable = false)
    @NotNull
    private Date from;

    @Column(name = "date_end", nullable = false)
    @NotNull
    private Date to;

    @Column(name = "is_active", nullable = false)
    @NotNull
    private boolean isActive = false;

    @ManyToOne
    @JoinColumn(name = "tariff_id", nullable = false)
    @NotNull
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    private User owner;

    public interface Fields{
        String TARIFF = "tariff";

        String OWNER = "owner";
    }
}
