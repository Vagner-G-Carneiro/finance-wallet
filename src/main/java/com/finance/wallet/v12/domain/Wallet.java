package com.finance.wallet.v12.domain;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "wallets")
@Table(name = "wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column (
                    name = "balance"
            )
    )
    private Money balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_status", nullable = false)
    private WalletStatus walletStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.balance = Money.zero();
        wallet.user = user;
        wallet.setWalletStatus(WalletStatus.ACTIVE);
        return wallet;
    }

    public void deposit(Money amount){
        if(!amount.isGreaterThan(Money.zero())){
            throw V12WalletException.businessRule("Impossivel depositar um valor menor ou igual a zero");
        }
        this.balance = this.balance.add(amount);
    }

    public void  withdraw(Money amount){
        if(this.balance.isLessThan(amount)){
            throw V12WalletException.businessRule("Valor de retirada maior que o valor em saldo da cateira.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
