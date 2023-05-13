package com.br.digitalmoneyhouse.user.model;

import com.br.digitalmoneyhouse.core.role.model.RoleEntity;
import com.br.digitalmoneyhouse.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String firstName;

    private String surname;

    private String cpf;

    @Column(length = 15)
    private String phoneNumber;

    @Column(nullable = false, unique = true) //email
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<RoleEntity> roles;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    @Column(length = 6)
    private String codeRecoverPassword;

    private Long dateExpireCodeRecover;

    @Column(unique = true, length = 36)
    private String codeEmailVerify;

    private Boolean activeEmail;

    public UserEntity(UserDTO userDTO, Set<RoleEntity> roles) {
        this.firstName = userDTO.getFirstName();
        this.surname = userDTO.getSurname();
        this.cpf = userDTO.getCpf();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.roles = roles;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.activeEmail = false;
        this.codeEmailVerify = null;
    }
}
