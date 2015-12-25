package com.vsthost.rnd.fiyat;

import java.time.temporal.Temporal;
import java.util.Currency;

/**
 * Provides a FX conversion interface for {@link Money} instances.
 */
public interface Converter {
    /**
     * Converts the {@link Money} instance to the given {@link Currency} as of the {@link Temporal} provided.
     *
     * @param money The {@link Money} instance to be converted.
     * @param to The currency to be converted to.
     * @param asof The time of exchange rate.
     * @return The converted {@link Money} instance.
     */
    Money convert(final Money money, final Currency to, final Temporal asof);

    /**
     * Converts the {@link Money} instance to the {@link Currency} provided.
     *
     * Note that the time is inherited from the {@link Money} instance provided, if any. If the {@link Money} instance
     * does not have a time property, the current time shall be used.
     *
     * @param money The {@link Money} instance to be converted.
     * @param to The currency to be converted to.
     * @return The converted {@link Money} instance.
     */
    Money convert(final Money money, final Currency to);

    /**
     * Indicates if a conversion is required.
     *
     * @param money The {@link Money} instance to be converted.
     * @param to The currency to be converted to.
     * @return `true` if conversion is required, `false` otherwise.
     */
    default boolean isConversionRequired (final Money money, final Currency to) {
        return !money.getCurrency().equals(to);
    }
}
