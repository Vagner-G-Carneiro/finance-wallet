package com.finance.wallet.v12.repository;

import com.finance.wallet.v12.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("""
            SELECT t FROM transactions t
            WHERE t.walletSender.id = :walletId
            OR t.walletReceiver.id = :walletId
            """)
    Page<Transaction> findBankStatement(UUID walletId, Pageable pageable);
}
