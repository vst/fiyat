package com.vsthost.rnd.fiyat;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

/**
 * Provides a test suite for the {@link Money} implementation.
 */
public class MoneyTest {
    /**
     * Defines a static currency instances for `USD`.
     */
    public static Currency USD = Currency.getInstance("USD");

    /**
     * Defines a static currency instances for `TRY`.
     */
    public static Currency TRY = Currency.getInstance("TRY");

    /**
     * Tests if the "null" currency raises appropriate exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullValueCurrency() {
        Money.of(null, BigDecimal.ONE);
    }

    /**
     * Tests if the "null" amount raises appropriate exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullValueAmount() {
        Money.of(USD, null);
    }

    /**
     * Tests if the "null" rounding mode raises appropriate exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullValueRounding() {
        Money.of(USD, BigDecimal.ONE, (RoundingMode) null);
    }

    /**
     * Tests for the equality of the two money instances without temporal semantics.
     */
    @Test
    public void equalityNonTemporal () {
        // Same currency, same singleton amounts:
        assertEquals(Money.of(USD, BigDecimal.ONE), Money.of(USD, BigDecimal.ONE));

        // Same currency, different amounts with same values:
        assertEquals(Money.of(USD, BigDecimal.valueOf(1)), Money.of(USD, BigDecimal.valueOf(100.0 / 100)));

        // Same currency, different amounts with difference scales but same values:
        assertEquals(Money.of(USD, new BigDecimal("10.0")), Money.of(USD, new BigDecimal("10.000")));

        // Same currency, different amounts with different extra decimals for rounding down:
        assertEquals(Money.of(USD, new BigDecimal("10.001")), Money.of(USD, new BigDecimal("10.00")));

        // Same currency, different amounts with different extra decimals for rounding up:
        assertEquals(Money.of(USD, new BigDecimal("10.005")), Money.of(USD, new BigDecimal("10.01")));

        // Same currency, different amounts with different extra decimals with explicit rounding:
        assertEquals(Money.of(USD, new BigDecimal("10.005"), RoundingMode.HALF_DOWN), Money.of(USD, new BigDecimal("10.00")));
    }

    @Test
    public void builder () {
        // Get a money:
        Money money1 = Money.builder()
                .currency(USD)
                .amount(BigDecimal.ONE)
                .build();

        // Get a second money:
        Money money2 = Money.builder(money1).build();

        // Are they equal?
        assertEquals(money1, money2);
    }

    @Test
    public void builderFull () {
        // Get a money:
        Money money1 = Money.builder()
                .currency(USD)
                .amount(BigDecimal.ONE)
                .time(Instant.now())
                .rate(Rate.of(TRY, USD, BigDecimal.ONE, Instant.now()))
                .build();

        // Get a second money:
        Money money2 = Money.builder(money1).build();

        // Are they equal?
        assertEquals(money1, money2);
    }
}
