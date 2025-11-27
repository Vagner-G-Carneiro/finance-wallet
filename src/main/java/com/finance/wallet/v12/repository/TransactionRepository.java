package com.finance.wallet.v12.repository;

import com.finance.wallet.v12.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
