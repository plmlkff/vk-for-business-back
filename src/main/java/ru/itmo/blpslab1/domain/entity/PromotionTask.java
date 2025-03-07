package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.blpslab1.domain.enums.PromotionType;

import java.util.UUID;

@Entity
@Table(name = "promotion_task")
@Getter
@Setter
public class PromotionTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String subject;

    @Column(nullable = false)
    @NotNull
    private String body;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved = false;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private PromotionType promotionType;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @NotNull
    private Group group;
}
