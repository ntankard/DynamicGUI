package com.ntankard.dynamicGUI.gui.util.decoder;

import java.text.NumberFormat;

public interface CurrencyDecoder_NumberFormatSource {

    /**
     * Get the Locale used for the currency to display for this row
     *
     * @param rowObject   The row the cell is in
     * @param contextName The name of the thing we are displaying for
     * @return The locale for the currency to display
     */
    NumberFormat getNumberFormat(Object rowObject, String contextName);
}
