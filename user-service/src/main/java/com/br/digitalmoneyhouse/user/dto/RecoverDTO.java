package com.br.digitalmoneyhouse.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecoverDTO {
    private String username;
    private String id;

    public RecoverDTO(String username, String id) {
        this.id = id;
        this.username = username;
    }
}
