package com.br.digitalmoney.accountservice.activity.entities;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "activities")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL )
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transaction;

    @OneToOne
    private AccountEntity account;

    private LocalDateTime dateTransaction;

    private Double value;

    private ActivityType activityType;

    private String description;
}
