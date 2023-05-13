package com.br.digitalmoney.accountservice.transactions.entity;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class TransactionEntity implements Comparable<TransactionEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private LocalDateTime date;
    private String description;

    @ManyToOne
    @JoinColumn(name="account_from_id", nullable=false)
    private AccountEntity accountFrom;

    @ManyToOne
    @JoinColumn(name="account_to_id", nullable=false)
    private AccountEntity accountTo;

    public int compareTo(TransactionEntity transactionEntity) {
        return this.getDate().compareTo(transactionEntity.getDate());
    }
}
