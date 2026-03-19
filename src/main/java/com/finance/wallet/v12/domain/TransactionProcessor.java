package com.finance.wallet.v12.domain;

import com.finance.wallet.v12.domain.db.entity.FinancialOperation;
import com.finance.wallet.v12.domain.db.entity.Transaction;
import com.finance.wallet.v12.domain.enums.OperationType;
import com.finance.wallet.v12.infra.exceptions.V12TransactionException;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

import static com.finance.wallet.v12.domain.enums.BusinessError.INVALID_AMOUNT;
import static com.finance.wallet.v12.domain.enums.BusinessError.SELF_TRANSACTION;

@Service
public class TransactionProcessor implements FinancialOperation<Wallet, Money> {


    @Override
    public Transaction transfer(final Wallet source, final Wallet target, final Money amount) {

        validate(source, target, amount);
        source.withdraw(amount);
        target.deposit(amount);
        /**
         * Aqui teria um pequeno problema, pois o ideal seria guardar no Transaction o valor atual antes das operacoes
         *  withdraw e deposit, para que voce consiga rastrear o que tinha na conta antes da operacao,
         *  mesmo se eu inverter e criar o transaction quando realizar o withdraw e deposit o valor do objeto sera atualizado
         *  inclusive suas referencias, o ideal seria criar uma copia dos objetos source e target e passar para o
         *  transaction.
         */
        return Transaction.builder()
                .source(source)
                .target(target)
                .amount(amount)
                .operationType(OperationType.TRANSFER)
                .build();
    }

    private static void validate(final Wallet source, final Wallet target, final Money amount) {
        if(!amount.isGreaterThan(Money.zero())){
            throw V12WalletException.businessRule("amount must not less to zero", INVALID_AMOUNT);
        }

        Stream.of(source, target).filter(Objects::nonNull).forEach(Wallet::validate);
        if(source.getId().equals(target.getId())){
            throw V12TransactionException.businessRule("Wallet id is same", SELF_TRANSACTION);
        }
    }

    @Override
    public Transaction deposit(Wallet target, Money amount) {
        validate(null, target, amount);
        target.deposit(amount);
        return Transaction.builder()
                .target(target)
                .amount(amount)
                .operationType(OperationType.DEPOSIT)
                .build();
    }

}
