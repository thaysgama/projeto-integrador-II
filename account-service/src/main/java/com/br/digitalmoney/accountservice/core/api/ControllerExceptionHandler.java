package com.br.digitalmoney.accountservice.core.api;

import com.br.digitalmoney.accountservice.accounts.exception.AccountNotFoundException;
import com.br.digitalmoney.accountservice.accounts.exception.UserNotFoundException;
import com.br.digitalmoney.accountservice.cards.exception.CardAlreadyRegisteredException;
import com.br.digitalmoney.accountservice.cards.exception.CardNotFoundException;
import com.br.digitalmoney.accountservice.transactions.exception.TransactionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> noHandlerFoundException(HttpServletRequest req) {
        final String error = String.format("Route %s not found", req.getRequestURI());

        return buildResponseEntity(HttpStatus.NOT_FOUND, "route_not_found", error);
    }

    @ExceptionHandler(CardAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handlerCardAlreadyRegisteredException(CardAlreadyRegisteredException e) {
        return buildResponseEntity(HttpStatus.CONFLICT, "card_already_registered", e.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlerAccountNotFoundException(AccountNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "account_not_found",  e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlerUserNotFoundException(UserNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "user_not_found", e.getMessage());
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlerCardNotFoundException(CardNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "card_not_found", e.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlerTransactionNotFoundException(TransactionNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "transaction_not_found", e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handlerUnauthorizedException(HttpClientErrorException.Unauthorized e) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, "user_unauthorized", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handlerUnknownException(Exception e) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "internal_error", e.getMessage() );
    }



    private ResponseEntity<Object> buildResponseEntity(final HttpStatus httpStatus,
                                                       final String message,
                                                       final String error) {
        final ApiError apiError = ApiError.builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
