package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Table(name = "tariff")
@Entity
@Getter
@Setter
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Double price;

    @Column(name = "preview_image_name")
    private String previewImageName;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @NotNull
    private Group group;

    @OneToMany(mappedBy = Subscription.Fields.TARIFF, cascade = CascadeType.ALL)
    private Set<Subscription> subscriptions;
}
