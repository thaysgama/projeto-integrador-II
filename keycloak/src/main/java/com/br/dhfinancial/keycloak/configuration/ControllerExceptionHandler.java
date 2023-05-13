package com.br.dhfinancial.keycloak.configuration;

import com.br.dhfinancial.keycloak.users.gateway.exception.KeycloakException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> noHandlerFoundException(HttpServletRequest req) {
        final String error = String.format("Route %s not found", req.getRequestURI());

        return buildResponseEntity(HttpStatus.NOT_FOUND, "route_not_found", error);
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerBadRequestException(Exception e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "bad_request", e.getMessage());
    }

    @ExceptionHandler(KeycloakException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlerCardAlreadyRegisteredException(KeycloakException e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "key_cloak_error", e.getMessage());
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
