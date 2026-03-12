package com.transaction.reward.repository;

import com.transaction.reward.entity.CustomerTxn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<CustomerTxn, Long> {

    List<CustomerTxn> findByCustomerIdAndTxnDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
