package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.OperationType;
import com.finance.wallet.v12.domain.Transaction;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.request.TransactionTransferDTO;
import com.finance.wallet.v12.dto.response.TransactionResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12TransactionException;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import com.finance.wallet.v12.repository.TransactionRepository;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public TransactionResponseDTO deposit(TransactionDepositDTO transation)
    {
        Wallet walletReceiver = this.walletRepository.findByIdWithLock(transation.walletReceiver())
                .orElseThrow(() -> V12WalletException.notFound("Carteira destinatária não encontrada."));

        Transaction depositTransaction = new Transaction();
        depositTransaction.setWalletReceiver(walletReceiver);
        depositTransaction.setAmount(transation.amount());
        depositTransaction.setCreatedAt(LocalDateTime.now());
        depositTransaction.setOperationType(OperationType.DEPOSIT);
        this.transactionRepository.save(depositTransaction);

        BigDecimal newAmount = walletReceiver.getBalance().add(transation.amount());
        walletReceiver.setBalance(newAmount);
        this.walletRepository.save(walletReceiver);
        return TransactionResponseDTO.fromEntity(depositTransaction);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferDTO transferDTO)
    {
        Wallet walletSender = this.walletRepository.findByIdWithLock(transferDTO.walletSender())
                .orElseThrow(() -> V12WalletException.notFound("Carteira remetente não encontrada, transferencia cancelada."));

        if(transferDTO.walletSender().equals(transferDTO.walletReceiver()))
        {
            throw V12TransactionException.businessRule("Impossível transferir dinheiro da própria carteira para ela mesma, transação negada!");
        }

        Wallet walletReceiver = this.walletRepository.findById(transferDTO.walletReceiver())
                .orElseThrow(() -> V12WalletException.businessRule("Carteira destinatária não encontrada, transferencia cancelada"));

        BigDecimal walletSenderNewBalance = walletSender.getBalance().subtract(transferDTO.amount());

        if(walletSenderNewBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            throw V12TransactionException.businessRule("Valor de transferência maior que saldo em conta, transação negada!");
        }

        walletSender.setBalance(walletSender.getBalance().subtract(transferDTO.amount()));
        walletReceiver.setBalance(walletReceiver.getBalance().add(transferDTO.amount()));
        this.walletRepository.save(walletReceiver);
        this.walletRepository.save(walletSender);

        Transaction transferTransaction = new Transaction();
        transferTransaction.setWalletSender(walletSender);
        transferTransaction.setWalletReceiver(walletReceiver);
        transferTransaction.setAmount(transferDTO.amount());
        transferTransaction.setCreatedAt(LocalDateTime.now());
        transferTransaction.setOperationType(OperationType.TRANSFER);
        this.transactionRepository.save(transferTransaction);

        return TransactionResponseDTO.fromEntity(transferTransaction);
    }

}
