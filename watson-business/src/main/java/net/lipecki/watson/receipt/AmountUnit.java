package net.lipecki.watson.receipt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum AmountUnit {

    KG("kg", Arrays.asList("kg", "KG")),
    L("l", Arrays.asList("l", "L")),
    UNIT("szt", Arrays.asList("szt", "SZT")),
    PACKAGE("op", Arrays.asList("op", "OP")),
    UNKNOWN("", Collections.emptyList());

    public static Optional<AmountUnit> getByAlias(final String alias) {
        return Arrays
                .stream(values())
                .filter(unit -> unit.matching(alias))
                .findFirst();
    }

    AmountUnit(final String name, final List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    private boolean matching(final String alias) {
        return this.aliases.contains(alias);
    }

    private final String name;
    private final List<String> aliases;

}

