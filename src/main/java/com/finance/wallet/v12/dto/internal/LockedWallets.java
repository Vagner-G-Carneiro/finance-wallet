package com.finance.wallet.v12.dto.internal;

import com.finance.wallet.v12.domain.Wallet;

public record LockedWallets(
        Wallet sender,
        Wallet receiver
){}
