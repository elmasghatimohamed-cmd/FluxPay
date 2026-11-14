package ma.fluxpay.finance.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldNormalizeValidAmount() {
        BigDecimal result = Money.requirePositive(
            new BigDecimal("250.5")
        );

        assertEquals(new BigDecimal("250.50"), result);
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Money.requireNonNegative(
                new BigDecimal("-10.00")
            )
        );
    }

    @Test
    void shouldRejectZeroForPositiveAmount() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Money.requirePositive(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectExcessivePrecision() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Money.requirePositive(
                new BigDecimal("10.001")
            )
        );
    }

    @Test
    void shouldRejectExcessiveAmount() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Money.requirePositive(
                new BigDecimal("100000000000000000.00")
            )
        );
    }

    @Test
    void shouldRejectNull() {
        assertThrows(
            NullPointerException.class,
            () -> Money.requirePositive(null)
        );
    }
}
