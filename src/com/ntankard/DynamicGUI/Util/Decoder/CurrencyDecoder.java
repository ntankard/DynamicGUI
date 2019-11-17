package com.ntankard.DynamicGUI.Util.Decoder;

import java.text.NumberFormat;

public class CurrencyDecoder extends DoubleDecoder {

    /**
     * The location used to get the currency
     */
    private NumberFormat numberFormat;

    /**
     * A user set source for the locale, numberFormat used if not set
     */
    private CurrencyDecoder_NumberFormatSource numberFormatSource = null;

    /**
     * The name of the thing we are displaying for
     */
    private String contextName;

    /**
     * Constructor
     */
    public CurrencyDecoder(NumberFormat numberFormat, String contextName, CurrencyDecoder_NumberFormatSource numberFormatSource) {
        this.numberFormat = numberFormat;
        this.contextName = contextName;
        this.numberFormatSource = numberFormatSource;
    }

    /**
     * Constructor
     */
    public CurrencyDecoder(NumberFormat numberFormat, String contextName) {
        this.numberFormat = numberFormat;
        this.contextName = contextName;
    }

    /**
     * Set A user set source for the locale
     *
     * @param numberFormatSource A user set source for the locale
     */
    public CurrencyDecoder setNumberFormatSource(CurrencyDecoder_NumberFormatSource numberFormatSource) {
        this.numberFormatSource = numberFormatSource;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Double value, Object rowObject) {
        if (value == null || value == 0) {
            return "-";
        }

        NumberFormat format = numberFormat;
        if (numberFormatSource != null && rowObject != null) {
            format = numberFormatSource.getNumberFormat(rowObject, contextName);
        }

        return format.format(value);
    }
}
