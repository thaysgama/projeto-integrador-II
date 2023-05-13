package com.br.digitalmoneyhouse.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CodeRecoverDTO {
    private String code;
    private String password;
    private String confirmPassword;

    public CodeRecoverDTO(String code, String password, String confirmPassword) {
        this.code = code;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
