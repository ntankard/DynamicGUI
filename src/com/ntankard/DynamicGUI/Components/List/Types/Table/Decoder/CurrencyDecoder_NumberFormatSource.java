package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.NumberFormat;

public interface CurrencyDecoder_NumberFormatSource {

    /**
     * Get the Locale used for the currency to display for this row
     *
     * @param rowObject The row the cell is in
     * @return The locale for the currency to display
     */
    NumberFormat getNumberFormat(Object rowObject);
}
