package com.ntankard.DynamicGUI.Util.Decoder;

public abstract class Decoder<T> {

    /**
     * Convert the object into a string to be displayed
     *
     * @param value     The value to convert
     * @param rowObject The row that this value is in. Used for custom formatting
     * @return The string to display
     */
    public abstract String decode(T value, Object rowObject);

    /**
     * Convert the string into a the original object
     *
     * @param value The value to convert
     * @return The object made from the string
     */
    public T encode(String value) {
        throw new RuntimeException("Should not be able to be called");
    }

    /**
     * Dose this decoder support Encoding (editing)?
     *
     * @return Dose this decoder support Encoding (editing)?
     */
    public boolean isEditable() {
        return false;
    }
}
