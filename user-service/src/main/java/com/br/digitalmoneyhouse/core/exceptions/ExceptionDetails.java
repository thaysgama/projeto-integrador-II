package com.br.digitalmoneyhouse.core.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
@Setter
public class ExceptionDetails {
    private String title;
    private String message;
    private Integer status;
    private Map<String, String> fieldErrors;
    private LocalDateTime timestamp;
}
