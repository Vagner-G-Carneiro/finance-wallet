package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.Money;
import com.finance.wallet.v12.domain.TransactionProcessor;
import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.domain.db.entity.Transaction;
import com.finance.wallet.v12.domain.db.repository.TransactionRepository;
import com.finance.wallet.v12.domain.enums.BusinessError;
import com.finance.wallet.v12.dto.internal.LockedWallets;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import com.finance.wallet.v12.dto.request.TransactionTransferDTO;
import com.finance.wallet.v12.dto.response.TransactionResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionProcessor transactionProcessor;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TransactionResponseDTO deposit(final TransactionDepositDTO dto) {
        Wallet target = this.walletRepository.findByIdWithLock(dto.walletReceiver())
                .orElseThrow(() -> V12WalletException.businessRule("Wallet ID %s not found"
                        .formatted(dto.walletReceiver()), BusinessError.WALLET_NOT_FOUND));

        var transaction = transactionRepository.save(transactionProcessor.deposit(target, Money.of(dto.amount())));
        return TransactionResponseDTO.fromEntity(transaction);
    }

    public TransactionResponseDTO transfer(final TransactionTransferDTO transferDTO, final User loggedUser) {
        Money amount = Money.of(transferDTO.amount());

        LockedWallets locked = lockedWallets(transferDTO.walletSender(), transferDTO.walletReceiver());
        Wallet sender = locked.sender();
        Wallet receiver = locked.receiver();
        validateLoggedUser(sender, loggedUser);

        var transaction = transactionRepository.save(transactionProcessor.transfer(sender, receiver, amount));
        return TransactionResponseDTO.fromEntity(transaction);
    }

    public Page<TransactionResponseDTO> bankStatement(final Pageable pageable, final User loggedUser) {
        Wallet wallet = this.walletRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> V12WalletException.businessRule("Transaction error", BusinessError.WALLET_NOT_FOUND));

        Page <Transaction> page = this.transactionRepository.findBankStatement(wallet.getId(), pageable);
        return page.map(TransactionResponseDTO::fromEntity);
    }

    private void validateLoggedUser(Wallet wallet, User loggedUser) {
        if(!wallet.getUser().getId().equals(loggedUser.getId())) {
            throw V12WalletException.businessRule("Transaction denied, logged user is invalid", BusinessError.INVALID_WALLET);
        }
    }

    private LockedWallets lockedWallets (UUID walletSender, UUID walletReceiver) {
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
            .orElseThrow(() -> V12WalletException.businessRule("Transaction error", BusinessError.WALLET_NOT_FOUND));

        Wallet secondWallet = this.walletRepository.findByIdWithLock(secondLock)
            .orElseThrow(() -> V12WalletException.businessRule("Transaction error", BusinessError.WALLET_NOT_FOUND));

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
