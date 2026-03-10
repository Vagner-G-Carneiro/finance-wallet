package com.finance.wallet.v12.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finance.wallet.v12.domain.Money;
import com.finance.wallet.v12.domain.Transaction;
import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.dto.internal.LockedWallets;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.request.TransactionTransferDTO;
import com.finance.wallet.v12.dto.response.TransactionResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import com.finance.wallet.v12.repository.TransactionRepository;
import com.finance.wallet.v12.repository.WalletRepository;

import jakarta.transaction.Transactional;

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
                .orElseThrow(() -> V12WalletException.notFound("Erro ao realizar transação, carteira inválida"));

        Money amount = Money.of(transation.amount());
        walletReceiver.deposit(amount);
        Transaction depositTransaction = Transaction.createDeposit(walletReceiver, amount);

        this.transactionRepository.save(depositTransaction);
        return TransactionResponseDTO.fromEntity(depositTransaction);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferDTO transferDTO, User loggedUser)
    {
        Money amount = Money.of(transferDTO.amount());

        if (transferDTO.walletSender().equals(transferDTO.walletReceiver())) {
            throw V12WalletException.businessRule("Carteira de envio igual à de recebimento.");
        }

        LockedWallets locked = lockedWallets(transferDTO.walletSender(), transferDTO.walletReceiver());
        Wallet sender = locked.sender();
        Wallet receiver = locked.receiver();

        validateLoggedUser(sender, loggedUser);

        Transaction transferTransaction = Transaction.createTransfer(sender, receiver, amount);
        this.transactionRepository.save(transferTransaction);
        return TransactionResponseDTO.fromEntity(transferTransaction);
    }

    public Page<TransactionResponseDTO> bankStatement(Pageable pageable, User loggedUser)
    {
        Wallet wallet = this.walletRepository.findByUserId(loggedUser.getId()).orElseThrow(() -> {
            throw V12WalletException.notFound("Carteira não encontrada");
        });

        Page <Transaction> pageEntitys = this.transactionRepository.findBankStatement(wallet
            .getId(), pageable);
        return pageEntitys.map(TransactionResponseDTO::fromEntity);
    }

    private void validateLoggedUser(Wallet wallet, User loggedUser)
    {
        if(!wallet.getUser().getId().equals(loggedUser.getId()))
        {
            throw V12WalletException.businessRule("Transação negada, carteira inválida!");
        }
    }

    private LockedWallets lockedWallets (UUID walletSender, UUID walletReceiver)
    {
        UUID firstLock;
        UUID secondLock;

        if(walletSender.compareTo(walletReceiver) < 0)
        {
            firstLock = walletSender;
            secondLock = walletReceiver;
        } else {
            firstLock = walletReceiver;
            secondLock = walletSender;
        }

        Wallet firstWallet = this.walletRepository.findByIdWithLock(firstLock)
            .orElseThrow(() -> V12WalletException.notFound("Erro de transação"));

        Wallet secondWallet = this.walletRepository.findByIdWithLock(secondLock)
            .orElseThrow(() -> V12WalletException.notFound("Erro de transação"));

        Wallet sender;
        Wallet receiver;

        if(firstWallet.getId().equals(walletSender))
        {
            sender = firstWallet;
            receiver = secondWallet;
        } else {
            sender = secondWallet;
            receiver = firstWallet;
        }

        return new LockedWallets(sender, receiver);
    }

}
