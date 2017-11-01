package net.lipecki.watson.expanse;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ExpanseCost {

    private BigDecimal amount;

    public ExpanseCost(final String amount) {
        this(new BigDecimal(amount));
    }

    private ExpanseCost(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        final DecimalFormat df = new DecimalFormat("0.00");
        return df.format(this.amount);
    }

    public ExpanseCost add(ExpanseCost other) {
        return new ExpanseCost(
                this.amount.add(other.amount)
        );
    }

    public static ExpanseCost empty() {
        return new ExpanseCost("0");
    }

}
