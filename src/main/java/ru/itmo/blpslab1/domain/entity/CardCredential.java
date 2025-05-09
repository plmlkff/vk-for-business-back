package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.Date;
import java.util.Set;
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
    @Size(min = 3)
    @Size(max = 3)
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    private User owner;

    @OneToMany(mappedBy = Tariff.Fields.RECIPIENT_CARD, cascade = CascadeType.ALL)
    private Set<Tariff> tariffs;

    @OneToMany(mappedBy = Goal.Fields.RECIPIENT_CARD)
    private Set<Goal> goals;

    interface Fields{
        String OWNER = "owner";
    }
}
