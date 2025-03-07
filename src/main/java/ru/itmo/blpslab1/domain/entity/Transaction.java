package ru.itmo.blpslab1.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.blpslab1.domain.enums.TransactionType;

import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    @NotNull
    private User payer;

    @ManyToOne
    @JoinColumn(name = "payer_card_id", nullable = false)
    @NotNull
    private CardCredential payerCard;

    @ManyToOne
    @JoinColumn(name = "recipient_card_id", nullable = false)
    @NotNull
    private CardCredential recipientCard;
}
