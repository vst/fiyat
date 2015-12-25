package com.vsthost.rnd.fiyat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.Temporal;
import java.util.Currency;
import java.util.Optional;

/**
 * Provides an immutable class which represents money objects with or without temporal and conversion semantics.
 *
 * By temporal semantics, it is meant that the value which the {@link Money} instance represents reflects a temporal
 * state. This class implementation assumes that `USD 10` *today* is different than `USD 10` *yesterday*.  The nominal
 * value of a {@link Money} instance can be the same as another {@link Money} instance at another time but this
 * semantics is managed by the client application.
 *
 * Note that the {@link Money#time} property is {@link Optional} which enables representation of money objects without
 * any temporal semantics.
 *
 * Similarly, a {@link Money} instance could have been converted from another {@link Money} instance with a different
 * currency. The rate of this possible conversion is represented by the {@link Optional} {@link Money#rate} property.
 *
 * Note that all the constructors of {@link Money} class are private. The only way to create {@link Money} instances is
 * through {@link Money#of} methods or the {@link Money.Builder} class instances.
 */
public final class Money <T extends Temporal> {
    /**
     * Defines the currency of the money instance.
     */
    private final Currency currency;

    /**
     * Defines the amount of the money instance.
     */
    private final BigDecimal amount;

    /**
     * Defines the optional time which the money instance represents the value as of.
     */
    private final Optional<T> time;

    /**
     * Defines the optional rate of the money instance if it has been converted from another money instance.
     */
    private final Optional<Rate> rate;

    /**
     * Provides a minimalistic private constructor for the {@link Money} class.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money properly scaled as in the ISO 4217 standards specification.
     */
    private Money(final Currency currency, final BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
        this.time = Optional.empty();
        this.rate = Optional.empty();
    }

    /**
     * Provides a private constructor for the {@link Money} class with temporal semantics.
     *
     * @param currency The currency of the money.
     * @param amount The amount of the money instance properly scaled as in the ISO 4217 standards specification.
     * @param time The time which the money instance represents the value as of.
     */
    private Money(final Currency currency, final BigDecimal amount, final T time) {
        this.currency = currency;
        this.amount = amount;
        this.time = Optional.of(time);
        this.rate = Optional.empty();
    }

    /**
     * Provides a private constructor for the {@link Money} class with conversion semantics.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money properly scaled as in the ISO 4217 standards specification.
     * @param rate The rate of the money instance at its conversion.
     */
    private Money(final Currency currency, final BigDecimal amount, final Rate rate) {
        this.currency = currency;
        this.amount = amount;
        this.time = Optional.empty();
        this.rate = Optional.of(rate);
    }

    /**
     * Provides a full-blown private constructor for the {@link Money} class with temporal and conversion semantics.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance properly scaled as in the ISO 4217 standards specification.
     * @param time The time which the money instance represents the value as of.
     * @param rate The rate of the money instance at its conversion.
     */
    private Money(final Currency currency, final BigDecimal amount, final T time, final Rate rate) {
        this.currency = currency;
        this.amount = amount;
        this.time = Optional.of(time);
        this.rate = Optional.of(rate);
    }

    /**
     * Returns the currency of the money instance.
     *
     * @return The currency of the money instance.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Returns the amount of the money instance.
     *
     * @return The amount of the money instance.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Returns the time which the money instance represents the value as of.
     *
     * @return The time which the money instance represents the value as of.
     */
    public Optional<T> getTime() {
        return time;
    }

    /**
     * Returns the rate of the money instance if it has been converted.
     *
     * @return The rate of the money instance if it has been converted.
     */
    public Optional<Rate> getRate() {
        return rate;
    }

    /**
     * Indicates if the provided argument is the same as this {@link Money} instance.
     *
     * Note that the internal implementation assumes that {@link Money#currency} and {@link Money#amount} fields can not
     * be null. Furthermore, as the {@link Money#amount} is a {@link BigDecimal} instance, we expect that the scales are
     * the same for a given currency.
     *
     * @param o The other object to check for equality.
     * @return `true` if the two {@link Money} instances are same in value, `false` otherwise.
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

        // Time should match if any:
        if (this.time.isPresent() ^ money.time.isPresent()) {
            return false;
        }
        else if (!this.time.equals(money.time)) {
            return false;
        }

        // Rate should match if any:
        if (this.rate.isPresent() ^ money.rate.isPresent()) {
            return false;
        }
        else if (!this.rate.equals(money.rate)) {
            return false;
        }

        // Amounts should match:
        return this.amount.equals(money.amount);
    }

    /**
     * Returns the hash code of the {@link Money} instance based on the hash codes of its data fields.
     *
     * @return The hash code of the {@link Money} instance.
     */
    @Override
    public int hashCode() {
        // Iterate over fields and compute the hash code accordingly:
        int hashcode = this.currency.hashCode();
        hashcode = 31 * hashcode + this.amount.hashCode();
        hashcode = 31 * hashcode + this.time.hashCode();
        hashcode = 31 * hashcode + this.rate.hashCode();

        // Done return the hash code:
        return hashcode;
    }

    /**
     * Creates an instance of {@link Money} class.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param time The time which the money instance represents the value as of.
     * @param rate The rate of the money instance at its conversion.
     * @param rounding The rounding mode to scale the amount as per ISO 4217 standards specification.
     * @return An instance of {@link Money} class.
     */
    public static <T extends Temporal> Money of (final Currency currency, final BigDecimal amount, final T time, final Rate rate, final RoundingMode rounding) {
        // Check if any of the arguments are null:
        if (currency == null || amount == null || time == null || rate == null || rounding == null) {
            throw new IllegalArgumentException(String.format("Can not create Money instances with null values: CCY=%s AMT=%s TIME=%s RATE=%s ROUND=%s", currency, amount, time, rate, rounding));
        }

        // First get the default number of digits after the decimal separator:
        final int decimals = currency.getDefaultFractionDigits();

        // Scale the amount, create the Money instance and return:
        return new Money(currency, amount.setScale(decimals, rounding), time, rate);
    }

    /**
     * Creates an instance of {@link Money} class.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param time The time which the money instance represents the value as of.
     * @param rounding The rounding mode to scale the amount as per ISO 4217 standards specification.
     * @return An instance of {@link Money} class.
     */
    public static <T extends Temporal> Money of (final Currency currency, final BigDecimal amount, final T time, final RoundingMode rounding) {
        // Check if any of the arguments are null:
        if (currency == null || amount == null || time == null || rounding == null) {
            throw new IllegalArgumentException(String.format("Can not create Money instances with null values: CCY=%s AMT=%s TIME=%s ROUND=%s", currency, amount, time, rounding));
        }

        // First get the default number of digits after the decimal separator:
        final int decimals = currency.getDefaultFractionDigits();

        // Scale the amount, create the Money instance and return:
        return new Money(currency, amount.setScale(decimals, rounding), time);
    }

    /**
     * Creates an instance of {@link Money} class.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param rate The rate of the money instance at its conversion.
     * @param rounding The rounding mode to scale the amount as per ISO 4217 standards specification.
     * @return An instance of {@link Money} class.
     */
    public static Money of (final Currency currency, final BigDecimal amount, final Rate rate, final RoundingMode rounding) {
        // Check if any of the arguments are null:
        if (currency == null || amount == null || rate == null || rounding == null) {
            throw new IllegalArgumentException(String.format("Can not create Money instances with null values: CCY=%s AMT=%s RATE=%s ROUND=%s", currency, amount, rate, rounding));
        }

        // First get the default number of digits after the decimal separator:
        final int decimals = currency.getDefaultFractionDigits();

        // Scale the amount, create the Money instance and return:
        return new Money(currency, amount.setScale(decimals, rounding), rate);
    }

    /**
     * Creates an instance of {@link Money} class.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param rounding The rounding mode to scale the amount as per ISO 4217 standards specification.
     * @return An instance of {@link Money} class.
     */
    public static Money of (final Currency currency, final BigDecimal amount, final RoundingMode rounding) {
        // Check if any of the arguments are null:
        if (currency == null || amount == null || rounding == null) {
            throw new IllegalArgumentException(String.format("Can not create Money instances with null values: CCY=%s AMT=%s ROUND=%s", currency, amount, rounding));
        }

        // First get the default number of digits after the decimal separator:
        final int decimals = currency.getDefaultFractionDigits();

        // Scale the amount, create the Money instance and return:
        return new Money(currency, amount.setScale(decimals, rounding));
    }

    /**
     * Provides an overloaded static method for {@link Money#of(Currency, BigDecimal, RoundingMode)}
     * with rounding mode set to {@link RoundingMode#HALF_UP}.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param time The time which the money instance represents the value as of.
     * @return An instance of {@link Money} class.
     */
    public static <T extends Temporal> Money of (final Currency currency, final BigDecimal amount, final T time, final Rate rate) {
        return Money.of(currency, amount, time, rate, RoundingMode.HALF_UP);
    }

    /**
     * Provides an overloaded static method for {@link Money#of(Currency, BigDecimal, RoundingMode)}
     * with rounding mode set to {@link RoundingMode#HALF_UP}.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param time The time which the money instance represents the value as of.
     * @return An instance of {@link Money} class.
     */
    public static <T extends Temporal> Money of (final Currency currency, final BigDecimal amount, final T time) {
        return Money.of(currency, amount, time, RoundingMode.HALF_UP);
    }

    /**
     * Provides an overloaded static method for {@link Money#of(Currency, BigDecimal, RoundingMode)}
     * with rounding mode set to {@link RoundingMode#HALF_UP}.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @param rate The rate of the money instance at its conversion.
     * @return An instance of {@link Money} class.
     */
    public static Money of (final Currency currency, final BigDecimal amount, final Rate rate) {
        return Money.of(currency, amount, rate, RoundingMode.HALF_UP);
    }

    /**
     * Provides an overloaded static method for {@link Money#of(Currency, BigDecimal, RoundingMode)}
     * with rounding mode set to {@link RoundingMode#HALF_UP}.
     *
     * @param currency The currency of the money instance.
     * @param amount The amount of the money instance.
     * @return An instance of {@link Money} class.
     */
    public static Money of (final Currency currency, final BigDecimal amount) {
        return Money.of(currency, amount, RoundingMode.HALF_UP);
    }

    public static <T extends Temporal> Builder builder() {
        return new Builder<T>();
    }

    public static Builder builder(final Money money) {
        return new Builder(money);
    }

    /**
     * Provides a builder class to build {@link Money} instances.
     */
    public static class Builder <T extends Temporal> {
        /**
         * Defines the currency of the money instance to be built.
         */
        private Currency currency;

        /**
         * Defines the amount of the money instance to be built.
         */
        private BigDecimal amount;

        /**
         * Defines the time of the money instance to be built, if any.
         */
        private T time;

        /**
         * Defines the rate of the money instance to be built, if any.
         */
        private Rate rate;

        /**
         * Provides the default constructor with no arguments.
         */
        public Builder () {}

        /**
         * Provides a copy builder.
         *
         * @param money The {@link Money} instance to be used to copy the values from.
         */
        public Builder (final Money<T> money) {
            this.currency = money.currency;
            this.amount = money.amount;
            this.time = money.time.isPresent() ? money.time.get() : null;
            this.rate = money.rate.isPresent() ? money.rate.get() : null;
        }

        /**
         * Sets the currency of the money instance to be built.
         *
         * @param currency The currency of the money instance to be built.
         * @return The {@link Builder} instance.
         */
        public Builder currency (final Currency currency) {
            // Check if the currency is null:
            if (currency == null) {
                throw new IllegalArgumentException("Currency can not be null.");
            }

            // Set the currency:
            this.currency = currency;

            // Return this builder:
            return this;
        }

        /**
         * Sets the amount of the money instance to be built.
         *
         * @param amount The amount of the money instance to be built.
         * @return The {@link Builder} instance.
         */
        public Builder amount (final BigDecimal amount) {
            // Check if the amount is null:
            if (amount == null) {
                throw new IllegalArgumentException("Amount can not be null.");
            }

            // Set the amount:
            this.amount = amount;

            // Return this builder:
            return this;
        }

        /**
         * Sets the time of the money instance to be built.
         *
         * @param time The time of the money instance to be built.
         * @return The {@link Builder} instance.
         */
        public Builder time (final T time) {
            // Check if the time is null:
            if (time == null) {
                throw new IllegalArgumentException("Time can not be null.");
            }

            // Set the time:
            this.time = time;

            // Return this builder:
            return this;
        }

        /**
         * Sets the rate of the money instance to be built.
         *
         * @param rate The rate of the money instance to be built.
         * @return The {@link Builder} instance.
         */
        public Builder rate (final Rate rate) {
            // Check if the rate is null:
            if (rate == null) {
                throw new IllegalArgumentException("Rate can not be null.");
            }

            // Set the rate:
            this.rate = rate;

            // Return this builder:
            return this;
        }

        /**
         * Builds and returns a {@link Money} instance.
         *
         * @return {@link Money} instance.
         */
        public Money build () {
            if (this.time == null && this.rate == null) {
                return Money.of(this.currency, this.amount);
            }
            else if (this.rate == null) {
                return Money.of(this.currency, this.amount, this.time);
            }
            else if (this.time == null) {
                return Money.of(this.currency, this.amount, this.rate);
            }
            return Money.of(this.currency, this.amount, this.time, this.rate);
        }
    }
}
