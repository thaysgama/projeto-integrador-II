package com.br.digitalmoney.accountservice.accounts.repository;

import com.br.digitalmoney.accountservice.accounts.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

}
