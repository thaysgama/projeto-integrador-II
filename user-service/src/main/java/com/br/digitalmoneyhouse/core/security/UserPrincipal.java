package com.br.digitalmoneyhouse.core.security;

import com.br.digitalmoneyhouse.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The type User principal.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPrincipal implements UserDetails {
    private UserEntity user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(!user.getRoles().isEmpty()){
            user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
