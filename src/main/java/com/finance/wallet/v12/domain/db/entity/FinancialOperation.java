package com.finance.wallet.v12.domain.db.entity;

public interface FinancialOperation<T, A> {

    Transaction transfer(T source, T target, A amount);

    Transaction deposit(T target, A amount);
}
