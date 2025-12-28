package com.finance.wallet.v12.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "wallets")
@Table(name = "wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_status", nullable = false)
    private WalletStatus walletStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
