package com.br.digitalmoneyhouse.user.dto;

import com.br.digitalmoneyhouse.user.model.UserEntity;
import jakarta.validation.constraints.*;
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
public class UserDTOUpdate {

    @NotBlank(message = "Name must not be empty.")
    private String firstName;

    @NotBlank(message = "Surname must not be empty.")
    private String surname;

    @Size(min = 11, max = 15)
    private String phoneNumber;

    @Email(message = "Username must be a valid email.")
    @NotBlank(message = "Username must not be empty.")
    private String username;

    /*
        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string
    * */
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 digit, 1 uppercase letter, 1 lowercase letter, 1 special character and be at least 8 characters long with no spaces.")
    @NotNull(message = "Password must not be null.")
    private String password;

    public UserDTOUpdate(UserEntity user) {
        this.firstName = user.getFirstName();
        this.surname = user.getSurname();
        this.phoneNumber = user.getPhoneNumber();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

}