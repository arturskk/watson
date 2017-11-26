package net.lipecki.watson.expanse;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ExpanseCost {

    public static ExpanseCost empty() {
        return new ExpanseCost("0");
    }

    public static ExpanseCost of(final String cost) {
        if (StringUtils.isBlank(cost)) {
            return empty();
        } else {
            return new ExpanseCost(cost.replaceAll(",", "."));
        }
    }

    private BigDecimal amount;

    public ExpanseCost(final String amount) {
        this(new BigDecimal(amount));
    }

    private ExpanseCost(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        final DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
        decimalSymbols.setDecimalSeparator('.');
        return new DecimalFormat("0.00", decimalSymbols).format(this.amount);
    }

    public ExpanseCost add(ExpanseCost other) {
        return new ExpanseCost(
                this.amount.add(other.amount)
        );
    }

}
