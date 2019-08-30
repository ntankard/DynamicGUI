package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

public interface Decoder {

    /**
     * Convert the object into a string to be displayed
     *
     * @param value The value to convert
     * @return The string to display
     */
    String decode(Object value);
}
