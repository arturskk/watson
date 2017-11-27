package net.lipecki.watson.expanse;

import lombok.Getter;
import net.lipecki.watson.WatsonException;
import org.apache.commons.lang.StringUtils;

@Getter
public class ExpanseCost {

    public static final ExpanseCost ZERO = new ExpanseCost(0L);

    public static ExpanseCost of(final String rawCost) {
        if (StringUtils.isBlank(rawCost)) {
            return ZERO;
        }
        final String cost = rawCost.replace(",", ".");
        if (cost.matches("\\d+\\.\\d\\d")) {
            return new ExpanseCost(parseCostWithSuffix(cost, ""));
        } else if (cost.matches("\\d+\\.\\d")) {
            return new ExpanseCost(parseCostWithSuffix(cost, "0"));
        } else if (cost.matches("\\d+")) {
            return new ExpanseCost(parseCostWithSuffix(cost, "00"));
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

    private final long amount;
    private final String description;

    private ExpanseCost(final long amount) {
        this.amount = amount;
        this.description = asDescription(amount);
    }

    public ExpanseCost add(final ExpanseCost other) {
        return new ExpanseCost(this.amount + other.amount);
    }

}
