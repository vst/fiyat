package com.vsthost.rnd.fiyat;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static junit.framework.TestCase.assertEquals;


/**
 * Provides a test suite for the {@link Money} implementation.
 */
public class MoneyTest {
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
        Money.of(CurrencyCode.AED, null);
    }

    /**
     * Tests if the "null" rounding mode raises appropriate exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullValueRounding() {
        Money.of(CurrencyCode.AED, BigDecimal.ONE, null);
    }

    /**
     * Tests for the equality of the two money instances.
     */
    @Test
    public void equality () {
        // Same currency, same singleton amounts:
        assertEquals(Money.of(CurrencyCode.AED, BigDecimal.ONE), Money.of(CurrencyCode.AED, BigDecimal.ONE));

        // Same currency, different amounts with same values:
        assertEquals(Money.of(CurrencyCode.AED, BigDecimal.valueOf(1)), Money.of(CurrencyCode.AED, BigDecimal.valueOf(100).divide(BigDecimal.valueOf(100))));

        // Same currency, different amounts with difference scales but same values:
        assertEquals(Money.of(CurrencyCode.AED, new BigDecimal("10.0")), Money.of(CurrencyCode.AED, new BigDecimal("10.000")));

        // Same currency, different amounts with different extra decimals for rounding down:
        assertEquals(Money.of(CurrencyCode.AED, new BigDecimal("10.001")), Money.of(CurrencyCode.AED, new BigDecimal("10.00")));

        // Same currency, different amounts with different extra decimals for rounding up:
        assertEquals(Money.of(CurrencyCode.AED, new BigDecimal("10.005")), Money.of(CurrencyCode.AED, new BigDecimal("10.01")));

        // Same currency, different amounts with different extra decimals with explicit rounding:
        assertEquals(Money.of(CurrencyCode.AED, new BigDecimal("10.005"), RoundingMode.HALF_DOWN), Money.of(CurrencyCode.AED, new BigDecimal("10.00")));
    }
}