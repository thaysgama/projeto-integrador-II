package com.br.digitalmoney.accountservice.transactions.controller;

import com.br.digitalmoney.accountservice.activity.assembler.ActivityMapper;
import com.br.digitalmoney.accountservice.activity.service.ActivityService;
import com.br.digitalmoney.accountservice.transactions.assembler.TransactionMapper;
import com.br.digitalmoney.accountservice.transactions.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.br.digitalmoney.utils.ActivityDataTest.getDefaultActivity;
import static com.br.digitalmoney.utils.ActivityDataTest.getDefaultActivityResponse;
import static com.br.digitalmoney.utils.TransactionDataTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionMapper transactionMapper;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private ActivityService activityService;
    @MockBean
    private ActivityMapper activityMapper;
    private final String URL = "/accounts";

    @Test
    void createShouldReturnCreated() throws Exception {
        long accountId = 1L;

        String bodyRequest = mapper.writeValueAsString(getDepositTransactionRequest());

        when(transactionMapper.fromTransactionRequestToTransaction(any())).thenReturn(getDepositTransaction());
        when(transactionService.create(anyLong(), any())).thenReturn(getDepositTransaction());
        when(transactionMapper.fromTransactionToTransactionResponse(any())).thenReturn(getDepositTransactionResponse());

        mockMvc.perform(post(URL + "/" + accountId + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void transferenceShouldReturnSuccess() throws Exception {
        long accountId = 1L;

        String bodyRequest = mapper.writeValueAsString(getTransferenceRequest());

        when(transactionMapper.fromTransferenceRequestToTransaction(any())).thenReturn(getTransferenceTransaction());
        when(transactionService.transference(anyLong(), any())).thenReturn(getTransferenceTransaction());
        when(transactionMapper.fromTransactionToTransactionResponse(any())).thenReturn(getTransferenceTransactionResponse());

        mockMvc.perform(post(URL + "/" + accountId + "/transference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest))
                .andExpect(status().isOk());
    }

    @Test
    void getTransferencesShouldReturnSuccess() throws Exception {
        long accountId = 1L;

        when(transactionService.findLastTransferencesByAccountID(any())).thenReturn(List.of(getTransferenceTransaction()));

        mockMvc.perform(get(URL + "/" + accountId + "/transferences")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTransactionsShouldReturnSuccess() throws Exception {
        long accountId = 1L;

        when(transactionService.findLastTransactionsByAccountID(any())).thenReturn(List.of(getTransferenceTransaction()));

        mockMvc.perform(get(URL + "/" + accountId + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAllActivitiesByAccountIdShouldReturnSuccess() throws Exception {
        long accountId = 1L;

        when(activityService.getAllActivitiesByIdAccounts(any())).thenReturn(List.of(getDefaultActivity()));
        when(activityMapper.fromActivityToActivityResponse(any())).thenReturn(getDefaultActivityResponse());

        mockMvc.perform(get(URL + "/" + accountId + "/activity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findActivityByAccoutAndTransfersShouldReturnSuccess() throws Exception {
        long accountId = 1L;
        long transferId = 333L;

        when(activityService.getActivityByTransferId(any(), any())).thenReturn(getDefaultActivity());
        when(activityMapper.fromActivityToActivityResponse(any())).thenReturn(getDefaultActivityResponse());

        mockMvc.perform(get(URL + "/" + accountId + "/activity/" + transferId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}