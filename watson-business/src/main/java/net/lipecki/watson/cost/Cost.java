package net.lipecki.watson.cost;

import lombok.Getter;
import net.lipecki.watson.WatsonException;
import org.apache.commons.lang.StringUtils;

@Getter
public class Cost {

    public static final Cost ZERO = new Cost(0L);

    private final long amount;
    private final String description;

    public static Cost of(final long amount) {
        return new Cost(amount);
    }

    public static Cost of(final String rawCost) {
        if (StringUtils.isBlank(rawCost)) {
            return ZERO;
        }
        final String cost = rawCost.replace(",", ".");
        if (cost.matches("\\d+\\.\\d\\d")) {
            return new Cost(parseCostWithSuffix(cost, ""));
        } else if (cost.matches("\\d+\\.\\d")) {
            return new Cost(parseCostWithSuffix(cost, "0"));
        } else if (cost.matches("\\d+")) {
            return new Cost(parseCostWithSuffix(cost, "00"));
        } else {
            throw WatsonException.of("Can't parse cost as any expected format").with("cost", cost);
        }
    }

    private static String asDescription(final long amount) {
        final String amountString = Long.toString(amount);
        if (amount >= 100) {
            return amountString.substring(0, amountString.length() - 2) + "." + amountString.substring(amountString.length() - 2);
        } else if (amount >= 10) {
            return "0." + amountString;
        } else {
            return "0.0" + amountString;
        }
    }

    private static long parseCostWithSuffix(final String cost, final String suffix) {
        return Long.parseLong(cost.replace(".", "") + suffix);
    }

    private Cost(final long amount) {
        this.amount = amount;
        this.description = asDescription(amount);
    }

    public Cost add(final Cost other) {
        return new Cost(this.amount + other.amount);
    }

}
