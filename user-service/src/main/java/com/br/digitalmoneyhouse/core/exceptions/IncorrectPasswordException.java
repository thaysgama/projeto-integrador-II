package com.br.digitalmoneyhouse.core.exceptions;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
