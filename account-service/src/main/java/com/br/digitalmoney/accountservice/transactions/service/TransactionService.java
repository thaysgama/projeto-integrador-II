package com.br.digitalmoney.accountservice.transactions.service;


import com.br.digitalmoney.accountservice.transactions.domain.Transaction;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionPage;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionSearchCriteria;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

    Transaction create(Long id, Transaction transaction);
    Transaction transference(Long id, Transaction transaction);

    List<Transaction> findLastTransferencesByAccountID(Long accountID);

    List<Transaction> findLastTransactionsByAccountID(Long accountID);

    Page<TransactionEntity> findByFilters(TransactionPage transactionPage, TransactionSearchCriteria transactionSearchCriteria);

}
