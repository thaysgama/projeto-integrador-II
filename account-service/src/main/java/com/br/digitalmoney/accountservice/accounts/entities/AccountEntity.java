package com.br.digitalmoney.accountservice.accounts.entities;

import com.br.digitalmoney.accountservice.cards.entities.CardEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private double availableAmount;
    private Long userId;
    @OneToMany(mappedBy="accountEntity")
    private List<CardEntity> cards;

    @OneToMany(mappedBy= "accountFrom")
    private List<TransactionEntity> transactionsFrom;

    @OneToMany(mappedBy= "accountTo")
    private List<TransactionEntity> transactionsTo;

    public void insertTransaction(double value) {
        this.availableAmount += value;
    }

    public void withdrawTransaction(double value) {
        this.availableAmount -= value;
    }

    public boolean hasNoBalance(double value) {
        return (this.availableAmount -= value) < 0;
    }
}
