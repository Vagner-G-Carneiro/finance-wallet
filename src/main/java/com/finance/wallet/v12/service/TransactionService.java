package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.*;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.request.TransactionTransferDTO;
import com.finance.wallet.v12.dto.response.TransactionResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12TransactionException;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import com.finance.wallet.v12.repository.TransactionRepository;
import com.finance.wallet.v12.repository.UserRepository;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository, UserRepository userRepository)
    {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;

    }

    @Transactional
    public TransactionResponseDTO deposit(TransactionDepositDTO transation)
    {
        Wallet walletReceiver = this.walletRepository.findByIdWithLock(transation.walletReceiver())
                .orElseThrow(() -> V12WalletException.notFound("Carteira destinatária não encontrada."));

        Money amount = Money.of(transation.amount());
        walletReceiver.deposit(amount);
        Transaction depositTransaction = Transaction.createDeposit(walletReceiver, amount);

        this.transactionRepository.save(depositTransaction);
        this.walletRepository.save(walletReceiver);
        return TransactionResponseDTO.fromEntity(depositTransaction);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferDTO transferDTO, User loggedUser)
    {
        Money amount = Money.of(transferDTO.amount());

        Wallet walletSender = this.walletRepository.findByIdWithLock(transferDTO.walletSender())
                .orElseThrow(() -> V12WalletException.notFound("Carteira remetente não encontrada, transferencia cancelada."));

        if(!walletSender.getUser().getId().equals(loggedUser.getId()))
        {
            throw V12WalletException.businessRule("Você só pode fazer tranferencias da sua própria carteira!");
        }

        if(!walletSender.getWalletStatus().equals(WalletStatus.ACTIVE))
        {
            throw V12WalletException.businessRule("Carteira remetente desativada.");
        }

        if(transferDTO.walletSender().equals(transferDTO.walletReceiver()))
        {
            throw V12TransactionException.businessRule("Impossível transferir dinheiro da própria carteira para ela mesma, transação negada!");
        }

        Wallet walletReceiver = this.walletRepository.findById(transferDTO.walletReceiver())
                .orElseThrow(() -> V12WalletException.businessRule("Carteira destinatária não encontrada, transferencia cancelada"));

        if(!walletReceiver.getWalletStatus().equals(WalletStatus.ACTIVE))
        {
            throw V12WalletException.businessRule("Carteira destinatária desativada.");
        }

        walletSender.withdraw(amount);
        walletReceiver.deposit(amount);
        Transaction transferTransaction = Transaction.createTransfer(walletSender, walletReceiver, amount);

        this.walletRepository.save(walletReceiver);
        this.walletRepository.save(walletSender);
        this.transactionRepository.save(transferTransaction);

        return TransactionResponseDTO.fromEntity(transferTransaction);
    }

    public Page<TransactionResponseDTO> bankStatement(UUID walletId, Pageable pageable, User loggedUser)
    {
        Wallet wallet = this.walletRepository.findById(walletId).orElseThrow(() -> {
            throw V12WalletException.notFound("Carteira não encontrada");
        });

        if(!wallet.getUser().getId().equals(loggedUser.getId()))
        {
            throw V12WalletException.businessRule("Você não pode emitir extrato de uma carteira que não é sua!");
        }
        Page <Transaction> pageEntitys = this.transactionRepository.findBankStatement(walletId, pageable);
        return pageEntitys.map(TransactionResponseDTO::fromEntity);
    }

}
