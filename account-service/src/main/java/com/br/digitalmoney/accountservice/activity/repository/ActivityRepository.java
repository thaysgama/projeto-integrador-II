package com.br.digitalmoney.accountservice.activity.repository;

import com.br.digitalmoney.accountservice.activity.entities.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {
    ActivityEntity findByAccountIdAndTransactionId(Long id, Long transactionId);
}
