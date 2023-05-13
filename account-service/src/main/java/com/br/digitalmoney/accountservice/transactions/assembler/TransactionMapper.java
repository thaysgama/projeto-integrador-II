package com.br.digitalmoney.accountservice.transactions.assembler;

import com.br.digitalmoney.accountservice.transactions.domain.Transaction;
import com.br.digitalmoney.accountservice.transactions.dto.request.TransactionRequest;
import com.br.digitalmoney.accountservice.transactions.dto.request.TransferenceRequest;
import com.br.digitalmoney.accountservice.transactions.dto.response.TransactionResponse;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "value", target = "amount")
    Transaction fromTransactionRequestToTransaction(TransactionRequest transactionRequest);

    @Mapping(source = "accountIDFrom", target = "accountFrom.id")
    @Mapping(source = "accountIDTo", target = "accountTo.id")
    TransactionEntity fromTransactionToTransactionEntity(Transaction transaction);
    @Mapping(source = "accountFrom.id", target = "accountIDFrom")
    @Mapping(source = "accountTo.id", target = "accountIDTo")
    Transaction fromTransactionEntityToTransaction(TransactionEntity transactionEntity);
    TransactionResponse fromTransactionToTransactionResponse(Transaction transaction);

    @Mapping(source = "value", target = "amount")
    Transaction fromTransferenceRequestToTransaction(TransferenceRequest transferenceRequest);

}
