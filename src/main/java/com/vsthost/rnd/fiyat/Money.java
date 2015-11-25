package com.vsthost.rnd.fiyat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Provides an immutable money class.
 */
public final class Money {
    /**
     * Defines the currency code of the money.
     */
    private final CurrencyCode currency;

    /**
     * Defines the amount of the money.
     */
    private final BigDecimal amount;

    /**
     * Provides a private constructor for the {@link Money} class instances.
     *
     * <p>
     *     Money class instances must be created with the static method {@link Money#of(CurrencyCode, BigDecimal, RoundingMode)}.
     * </p>
     *
     * @param currency The currency code of the money.
     * @param amount The amount of the money properly scaled as in the ISO 4217 standards specification.
     */
    private Money(CurrencyCode currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    /**
     * Returns the currency of the money.
     *
     * @return The currency of the money.
     */
    public CurrencyCode getCurrency() {
        return currency;
    }

    /**
     * Returns the amount of the money.
     *
     * @return The amount of the money.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Indicates if the provided argument is the same as this {@link Money} instance.
     *
     * <p>
     *     Note that the internal implementation assumes that {@link Money#currency} and
     *     {@link Money#amount} fields can not be null. Furthermore, as the {@link Money#amount}
     *     is a {@link BigDecimal} instance, we expect that the scales are the same for a given
     *     currency code.
     * </p>
     *
     * @param o The other object to check for equality.
     * @return <code>true</code> if the two {@link Money} instances are same in value, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        // Are these same objects?
        if (this == o) {
            return true;
        }

        // Is the argument null or are the classes different?
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        // Cast the argument to Money:
        final Money money = (Money) o;

        // Currencies should match:
        if (this.currency != money.currency) {
            return false;
        }

        // Amounts should match:
        return this.amount.equals(money.amount);
    }

    /**
     * Returns the hash code of the {@link Money} instance based on the hash code of
     * the hash codes of {@link Money#currency} and {@link Money#amount} fields.
     *
     * @return The hash code of the {@link Money} instance.
     */
    @Override
    public int hashCode() {
        return 31 * this.currency.hashCode() + this.amount.hashCode();
    }

    /**
     * Creates an instance of {@link Money} class.
     *
     * @param currency The currency code of the money.
     * @param amount The amount of the money.
     * @param rounding The rounding mode to scale the amount as per ISO 4217 standards specification.
     * @return An instance of {@link Money} class.
     */
    public static Money of (CurrencyCode currency, BigDecimal amount, RoundingMode rounding) {
        // Check if any of the arguments are null:
        if (currency == null || amount == null || rounding == null) {
            throw new IllegalArgumentException(String.format("Can not create Money instances with null values: CCY=%s AMT=%s ROUND=%s", currency, amount, rounding));
        }

        // First get the default number of digits after the decimal separator:
        final int decimals = Currency.getInstance(currency.name()).getDefaultFractionDigits();

        // Scale the amount, create the Money instance and return:
        return new Money(currency, amount.setScale(decimals, rounding));
    }

    /**
     * Provides an overloaded static method for {@link Money#of(CurrencyCode, BigDecimal, RoundingMode)}
     * with rounding mode set to {@link RoundingMode#HALF_UP}.
     *
     * @param currency The currency code of the money.
     * @param amount The amount of the money.
     * @return An instance of {@link Money} class.
     */
    public static Money of (CurrencyCode currency, BigDecimal amount) {
        return Money.of(currency, amount, RoundingMode.HALF_UP);
    }
}
