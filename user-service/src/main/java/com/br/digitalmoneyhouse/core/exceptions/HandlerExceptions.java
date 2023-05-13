package com.br.digitalmoneyhouse.core.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class HandlerExceptions {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex){
        List<FieldError> errors = ex.getFieldErrors();
        Map<String, String> fieldErrors = new HashMap<>();
        errors.forEach(
                err -> fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Parâmetros inválidos")
                        .message("Um ou mais campos com valor inválido.")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .fieldErrors(fieldErrors)
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDetails> handleDataIntegrityViolationException (DataIntegrityViolationException ex){
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Dados inválidos")
                        .message("Um ou mais valores fornecidos são inválidos")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDetails> handlerNotFoundException(NotFoundException ex){
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .title("Objeto não encontrado")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ExceptionDetails> handlerIncorrectPasswordException(IncorrectPasswordException ex){
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .title("Senha incorreta")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionDetails> handlerInvalidTokenException(InvalidTokenException ex){
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .title("Token inválido.")
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ExceptionDetails> handlerNotUniqueException(NotUniqueException ex){
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .title("Objeto já cadastrado")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, HttpStatus.CONFLICT);
    }
}
