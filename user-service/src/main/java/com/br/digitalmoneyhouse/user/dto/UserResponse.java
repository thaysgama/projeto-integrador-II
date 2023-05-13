package com.br.digitalmoneyhouse.user.dto;

import com.br.digitalmoneyhouse.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type User dto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String firstName;
    private String surname;
    private String cpf;
    private String phoneNumber;
    private String username;

    public UserResponse(UserEntity user) {
        this.firstName = user.getFirstName();
        this.surname = user.getSurname();
        this.cpf = user.getCpf();
        this.phoneNumber = user.getPhoneNumber();
        this.username = user.getUsername();
    }

}