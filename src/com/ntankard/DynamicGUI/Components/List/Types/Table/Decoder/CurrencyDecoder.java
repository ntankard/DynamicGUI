package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyDecoder implements Decoder {

    /**
     * The location used to get the currency
     */
    private Locale currencyLocale;

    /**
     * Constructor
     *
     * @param currencyLocale The location used to get the currency
     */
    public CurrencyDecoder(Locale currencyLocale) {
        this.currencyLocale = currencyLocale;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(currencyLocale);
        return format.format(value);
    }
}
