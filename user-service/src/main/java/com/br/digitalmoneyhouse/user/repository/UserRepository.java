package com.br.digitalmoneyhouse.user.repository;

import com.br.digitalmoneyhouse.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);
    Boolean existsByUsername(String username);

    Optional<UserEntity> findByCodeEmailVerify(String codeEmailVerify);
}
