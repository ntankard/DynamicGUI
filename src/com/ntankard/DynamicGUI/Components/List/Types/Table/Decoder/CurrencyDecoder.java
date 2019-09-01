package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyDecoder extends DoubleDecoder {

    /**
     * The location used to get the currency
     */
    private Locale currencyLocale;

    /**
     * A user set source for the locale, currencyLocale used if not set
     */
    private CurrencyDecoder_LocaleSource localeSource = null;

    /**
     * Constructor
     *
     * @param currencyLocale The location used to get the currency
     */
    public CurrencyDecoder(Locale currencyLocale) {
        this.currencyLocale = currencyLocale;
    }

    /**
     * Set A user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public void setLocaleInspector(CurrencyDecoder_LocaleSource localeSource) {
        this.localeSource = localeSource;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value, Object rowObject) {
        Locale locale = currencyLocale;
        if (localeSource != null) {
            locale = localeSource.getLocale(rowObject);
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(value);
    }
}
