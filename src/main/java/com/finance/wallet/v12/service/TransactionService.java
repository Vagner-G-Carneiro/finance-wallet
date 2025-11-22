package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.OperationType;
import com.finance.wallet.v12.domain.Transaction;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.response.TransactionDepositResponseDTO;
import com.finance.wallet.v12.repository.TransactionRepository;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository)
    {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;

    }

    @Transactional
    public TransactionDepositResponseDTO deposit(TransactionDepositDTO transation)
    {
        Wallet walletReceiver = this.walletRepository.findById(transation.walletReceiver())
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada, deposito cancelado."));

        Transaction depositTransaction = new Transaction();
        depositTransaction.setWalletReceiver(walletReceiver);
        depositTransaction.setAmount(transation.amount());
        depositTransaction.setCreatedAt(LocalDateTime.now());
        depositTransaction.setOperationType(OperationType.DEPOSIT);
        System.out.println("TIPO DE OPERAÇÃO => " + depositTransaction.getOperationType());
        this.transactionRepository.save(depositTransaction);

        System.out.println("UUID deposit => " + depositTransaction.getId());

        BigDecimal newAmount = walletReceiver.getBalance().add(transation.amount());
        walletReceiver.setBalance(newAmount);
        this.walletRepository.save(walletReceiver);
        return TransactionDepositResponseDTO.fromEntity(depositTransaction);
    }

}
