package ma.fluxpay.finance.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2;
    private static final BigDecimal MAX_AMOUNT =
        new BigDecimal("99999999999999999.99");

    private Money() {
    }

    public static BigDecimal requirePositive(BigDecimal amount) {
        BigDecimal validated = requireNonNegative(amount);

        if (validated.signum() == 0) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero"
            );
        }

        return validated;
    }

    public static BigDecimal requireNonNegative(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount is required");

        BigDecimal normalized;

        try {
            normalized = amount.setScale(
                SCALE,
                RoundingMode.UNNECESSARY
            );
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                "Amount must have at most two decimal places",
                exception
            );
        }

        if (normalized.signum() < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative"
            );
        }

        if (normalized.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                "Amount exceeds the supported financial limit"
            );
        }

        return normalized;
    }
}
