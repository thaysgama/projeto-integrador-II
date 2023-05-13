package com.br.digitalmoney.accountservice.transactions.repository;

import com.br.digitalmoney.accountservice.transactions.dto.TransactionPage;
import com.br.digitalmoney.accountservice.transactions.dto.TransactionSearchCriteria;
import com.br.digitalmoney.accountservice.transactions.entity.TransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
public class TransactionCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public TransactionCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<TransactionEntity> findAllWithFilters(TransactionPage transactionPage,
                                                      TransactionSearchCriteria transactionSearchCriteria) {
        CriteriaQuery<TransactionEntity> criteriaQuery = criteriaBuilder.createQuery(TransactionEntity.class);
        Root<TransactionEntity> transactionRoot = criteriaQuery.from(TransactionEntity.class);
        Predicate predicate = getPredicate(transactionSearchCriteria, transactionRoot);
        criteriaQuery.where(predicate);
        setOrder(transactionPage, criteriaQuery, transactionRoot);

        TypedQuery<TransactionEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(transactionPage.getPageNumber() * transactionPage.getPageSize());
        typedQuery.setMaxResults(transactionPage.getPageSize());

        Pageable pageable = getPageable(transactionPage);

        long transactionsCount = getTransactionsCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, transactionsCount);
    }



    private Predicate getPredicate(TransactionSearchCriteria transactionSearchCriteria,
                                   Root<TransactionEntity> transactionRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (nonNull(transactionSearchCriteria.getStartDate()) && nonNull(transactionSearchCriteria.getEndDate())) {
            predicates.add(criteriaBuilder.between(transactionRoot.get("date"), transactionSearchCriteria.getStartDate(), transactionSearchCriteria.getEndDate()));
        } else if (nonNull(transactionSearchCriteria.getStartDate())) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("date"), transactionSearchCriteria.getStartDate()));
        } else if (nonNull(transactionSearchCriteria.getEndDate())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("date"), transactionSearchCriteria.getEndDate()));
        }

        if (nonNull(transactionSearchCriteria.getMinAmount()) && nonNull(transactionSearchCriteria.getMaxAmount())) {
            predicates.add(criteriaBuilder.between(transactionRoot.get("amount"), transactionSearchCriteria.getMinAmount(), transactionSearchCriteria.getMaxAmount()));
        } else if (nonNull(transactionSearchCriteria.getMinAmount())) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(transactionRoot.get("amount"), transactionSearchCriteria.getMinAmount()));
        } else if (nonNull(transactionSearchCriteria.getMaxAmount())) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(transactionRoot.get("amount"), transactionSearchCriteria.getMaxAmount()));
        }

        if (nonNull(transactionSearchCriteria.getTransactionType())) {
            predicates.add(criteriaBuilder.equal(transactionRoot.get("transactionType"), transactionSearchCriteria.getTransactionType()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(TransactionPage transactionPage,
                          CriteriaQuery<TransactionEntity> criteriaQuery,
                          Root<TransactionEntity> transactionRoot) {
        if(transactionPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(transactionRoot.get(transactionPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(transactionRoot.get(transactionPage.getSortBy())));
        }
    }


    private Pageable getPageable(TransactionPage transactionPage) {
        Sort sort = Sort.by(transactionPage.getSortDirection(), transactionPage.getSortBy());
        return PageRequest.of(transactionPage.getPageNumber(), transactionPage.getPageSize(), sort);
    }

    private long getTransactionsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<TransactionEntity> countRoot = countQuery.from(TransactionEntity.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
