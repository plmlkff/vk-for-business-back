package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "card_credential")
@Getter
@Setter
public class CardCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreditCardNumber
    @Size(min = 16, max = 16)
    @NotNull
    private String cardNumber;

    @Column(name = "end_date", nullable = false)
    @NotNull
    private Date endDate;

    @Column(nullable = false)
    @NotNull
    private Short cvv;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    private User owner;
}
