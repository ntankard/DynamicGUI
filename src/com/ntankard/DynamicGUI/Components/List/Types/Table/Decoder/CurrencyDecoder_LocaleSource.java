package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.util.Locale;

public interface CurrencyDecoder_LocaleSource {

    /**
     * Get the Locale used for the currency to display for this row
     *
     * @param rowObject The row the cell is in
     * @return The locale for the currency to display
     */
    Locale getLocale(Object rowObject);
}
