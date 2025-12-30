package com.finance.wallet.v12.domain;

import com.finance.wallet.v12.infra.exceptions.V12MoneyException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class Money implements Comparable<Money>{

    @Column(name = "value", precision = 19, scale = 2, nullable = false)
    private BigDecimal value;

    protected Money() {}

    private Money (BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Money zero () {
        return new Money(BigDecimal.ZERO);
    }

    public static Money of(BigDecimal value) {
        if(value == null || value.scale() > 2)
        {
            throw V12MoneyException.businessRule("Formato inválido. Criação do objeto Money impossibilitada.");
        }
        return new Money(value);
    }

    public Money add(Money otherMoney){
        return new Money(this.value.add(otherMoney.value));
    }

    public Money subtract(Money otherMoney){
        return new Money(this.value.subtract(otherMoney.value));
    }

    public final BigDecimal toBigDecimal(){
        return this.value;
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }

    public boolean isLessThan(Money other) {
        return this.value.compareTo(other.value) < 0;
    }

    public boolean isGreaterThan(Money other) {
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isEquals(Money other) {
        return this.value.compareTo(other.value) == 0;
    }
}
