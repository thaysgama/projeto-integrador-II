package com.br.digitalmoney.accountservice.cards.entities;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String number;
    private String name;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    private LocalDateTime dateExpire;
    private String cvc;

    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    private AccountEntity accountEntity;
}
