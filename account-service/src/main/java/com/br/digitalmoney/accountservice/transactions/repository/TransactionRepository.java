package com.br.digitalmoney.accountservice.transactions.repository;

import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

    List<TransactionEntity> findFirst10ByAccountFromIdAndTransactionTypeOrderByDateDesc(Long accountId, TransactionType transactionType);

    List<TransactionEntity> findFirst5ByAccountFromIdOrAccountToIdOrderByDateDesc(Long accountIdFrom, Long accountIdTo);
}

