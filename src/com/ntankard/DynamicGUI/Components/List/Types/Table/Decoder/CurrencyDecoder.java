package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

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
     * Constructor
     *
     * @param numberFormat The location used to get the currency
     */
    public CurrencyDecoder(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    /**
     * Set A user set source for the locale
     *
     * @param numberFormatSource A user set source for the locale
     */
    public void setNumberFormatSource(CurrencyDecoder_NumberFormatSource numberFormatSource) {
        this.numberFormatSource = numberFormatSource;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value, Object rowObject) {
        if ((Double) value == 0) {
            return "-";
        }

        NumberFormat format = numberFormat;
        if (numberFormatSource != null) {
            format = numberFormatSource.getNumberFormat(rowObject);
        }

        return format.format(value);
    }
}
