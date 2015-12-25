package com.vsthost.rnd.fiyat;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Currency;

/**
 * Provides an FX conversion rate class.
 */
public class Rate <T extends Temporal> {
    /**
     * Defines the currency to convert from.
     */
    private final Currency from;

    /**
     * Defines the currency to convert to.
     */
    private final Currency to;

    /**
     * Defines the value of the conversion rate.
     */
    private final BigDecimal value;

    /**
     * Defines the time of the conversion.
     */
    private final T time;

    /**
     * Provides a private constructor for the exchange rate.
     *
     * @param from The currency to convert from.
     * @param to The currency to convert to.
     * @param value The value of the conversion rate.
     * @param time The time of the conversion
     */
    private Rate (final Currency from, final Currency to, final BigDecimal value, final T time) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.time = time;
    }

    /**
     * Returns the currency to convert from.
     *
     * @return The currency to convert from.
     */
    public Currency getFrom() {
        return from;
    }

    /**
     * Returns the currency to convert to.
     *
     * @return The currency to convert to.
     */
    public Currency getTo() {
        return to;
    }

    /**
     * Returns the value of the conversion rate.
     *
     * @return The value of the conversion rate.
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Returns the time of the conversion.
     *
     * @return The time of the conversion.
     */
    public T getTime() {
        return time;
    }

    /**
     * Provides a static method to create conversion rate instances.
     *
     * @param from The currency to convert from.
     * @param to The currency to convert to.
     * @param value The value of the conversion rate.
     * @param time The time of the conversion
     * @return A {@link Rate} instance.
     */
    public static <T extends Temporal> Rate of (final Currency from, final Currency to, final BigDecimal value, final T time) {
        // Check if any of the arguments are null:
        if (from == null || to == null || value == null || time == null) {
            throw new IllegalArgumentException(String.format("Can not create rate instances with null values: FROM=%s TO=%s VALUE=%s TIME=%s", from, to, value, time));
        }

        // Construct the rate instance and return:
        return new Rate(from, to, value, time);
    }
}
