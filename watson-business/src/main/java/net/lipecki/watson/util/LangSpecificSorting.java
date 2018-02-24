package net.lipecki.watson.util;

import java.text.Collator;
import java.util.Locale;

public class LangSpecificSorting {

    public static final Collator WITH_LANG_PL = Collator.getInstance(Locale.forLanguageTag("pl"));

}
