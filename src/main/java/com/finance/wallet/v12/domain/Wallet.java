package com.finance.wallet.v12.domain;

import com.finance.wallet.v12.domain.db.entity.AbstractEntity;
import com.finance.wallet.v12.domain.enums.BusinessError;
import com.finance.wallet.v12.domain.enums.WalletStatus;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true, builderClassName = "Builder")
@Table(name = "wallets")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet extends AbstractEntity {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "balance"))
    private Money balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_status", nullable = false)
    private WalletStatus walletStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void deposit(Money amount) {
        if (!amount.isGreaterThan(Money.zero())) {
            throw V12WalletException.businessRule("Balance is less than zero", BusinessError.INVALID_AMOUNT);
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(final Money amount) {
        Objects.requireNonNull(amount, "Amount must not be null.");
        if (this.balance.isLessThan(amount)) {
            throw V12WalletException.businessRule("Balance is less than %d".formatted(amount.toBigDecimal().intValue()), BusinessError.INSUFFICIENT_BALANCE);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void validate() {
        if (walletStatus == WalletStatus.ACTIVE)
            throw V12WalletException.businessRule("Wallet ID %s is invalid ".formatted(id), BusinessError.INVALID_WALLET_STATUS);
    }

}
