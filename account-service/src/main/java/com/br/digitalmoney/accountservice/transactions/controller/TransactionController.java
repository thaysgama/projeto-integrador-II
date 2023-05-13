package com.br.digitalmoney.accountservice.transactions.controller;

import com.br.digitalmoney.accountservice.activity.assembler.ActivityMapper;
import com.br.digitalmoney.accountservice.activity.domains.Activity;
import com.br.digitalmoney.accountservice.activity.dto.response.ActivityResponse;
import com.br.digitalmoney.accountservice.activity.service.ActivityService;
import com.br.digitalmoney.accountservice.core.api.OpenApi;
import com.br.digitalmoney.accountservice.core.web.CheckAuthorization;
import com.br.digitalmoney.accountservice.transactions.assembler.TransactionMapper;
import com.br.digitalmoney.accountservice.transactions.domain.Transaction;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionPage;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionSearchCriteria;
import com.br.digitalmoney.accountservice.transactions.dto.request.TransactionRequest;
import com.br.digitalmoney.accountservice.transactions.dto.request.TransferenceRequest;
import com.br.digitalmoney.accountservice.transactions.dto.response.TransactionResponse;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionType;
import com.br.digitalmoney.accountservice.transactions.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class TransactionController implements OpenApi {

    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @PostMapping("/{accountID}/transactions")
    @CheckAuthorization
    ResponseEntity<TransactionResponse> create(
            @PathVariable Long accountID,
            @RequestBody @Valid TransactionRequest transactionRequest) {
        final Transaction transaction = transactionMapper
                .fromTransactionRequestToTransaction(transactionRequest);
        final Transaction result = transactionService.create(accountID, transaction);
        final TransactionResponse transactionResponse = transactionMapper.fromTransactionToTransactionResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionResponse);
    }

    @PostMapping("{accountID}/transference")
    @CheckAuthorization
    ResponseEntity<TransactionResponse> transference(@PathVariable Long accountID, @RequestBody @Valid TransferenceRequest transferenceRequest) {
        final Transaction transaction = transactionMapper
                .fromTransferenceRequestToTransaction(transferenceRequest);
        final Transaction result = transactionService.transference(accountID, transaction);
        final TransactionResponse transactionResponse = transactionMapper.fromTransactionToTransactionResponse(result);

        return ResponseEntity.ok(transactionResponse);
    }

    @GetMapping("{accountID}/transferences")
    @CheckAuthorization
    ResponseEntity<List<TransactionResponse>> getTransferences(@PathVariable Long accountID) {
        final List<TransactionResponse> lastTransactions = transactionService.findLastTransferencesByAccountID(accountID)
                .stream().map((t) -> transactionMapper.fromTransactionToTransactionResponse(t))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lastTransactions);
    }

    @GetMapping("{accountID}/transactions")
    @CheckAuthorization
    ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long accountID) {
        final List<TransactionResponse> lastTransactions = transactionService.findLastTransactionsByAccountID(accountID)
                .stream().map((t) -> transactionMapper.fromTransactionToTransactionResponse(t))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lastTransactions);
    }

    @GetMapping("{id}/activity")
    @CheckAuthorization
    ResponseEntity<List<ActivityResponse>> findAllActivitiesByAccountId(@PathVariable Long id) {
        final List<Activity> result = activityService.getAllActivitiesByIdAccounts(id);
        final List<ActivityResponse> activityResponses = new ArrayList<>();
        for (Activity activity : result) {
            activityResponses.add(activityMapper.fromActivityToActivityResponse(activity));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(activityResponses);
    }

    @GetMapping("{id}/activity/{transfersId}")
    @CheckAuthorization
    ResponseEntity<ActivityResponse> findActivityByAccoutAndTransfers(@PathVariable Long id, @PathVariable Long transfersId) {
        final Activity result = activityService.getActivityByTransferId(id, transfersId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(activityMapper.fromActivityToActivityResponse(result));
    }

//    @GetMapping("{accountID}/activity")
//    ResponseEntity<Page<TransactionEntity>> findByFilters(@RequestParam(name = "page", defaultValue = "0") int page,
//                                                          @RequestParam(name = "size", defaultValue = "10") int size,
//                                                          @RequestParam(name = "sortBy", defaultValue = "transactionType") String sortBy,
//                                                          @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
//                                                          @RequestParam(name = "startDate", required = false)
//                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//                                                          @RequestParam(name = "endDate", required = false)
//                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
//                                                          @RequestParam(name = "minAmount", required = false) Double minAmount,
//                                                          @RequestParam(name = "maxAmount", required = false) Double maxAmount,
//                                                          @RequestParam(name = "transactionType", required = false) TransactionType transactionType
//    ) {
//        TransactionPage transactionPage = new TransactionPage(page, size, Sort.Direction.fromString(sortOrder), sortBy);
//        TransactionSearchCriteria searchCriteria = new TransactionSearchCriteria();
//        searchCriteria.setStartDate(startDate);
//        searchCriteria.setEndDate(endDate);
//        searchCriteria.setMinAmount(minAmount);
//        searchCriteria.setMaxAmount(maxAmount);
//        searchCriteria.setTransactionType(transactionType);
//
//        Page<TransactionEntity> result = transactionService.findByFilters(transactionPage, searchCriteria);
//        return ResponseEntity.ok(result);
//    }
}


