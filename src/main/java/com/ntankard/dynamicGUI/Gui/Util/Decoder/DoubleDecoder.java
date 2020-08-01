package com.ntankard.dynamicGUI.Gui.Util.Decoder;

public class DoubleDecoder extends Decoder<Double> {

    /**
     * The number of decimal places to display
     */
    private int decimal;

    /**
     * Constructor
     */
    public DoubleDecoder(int decimal) {
        this.decimal = decimal;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Double value, Object rowObject) {
        return String.format("%." + decimal + "f", value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Double encode(String value) {
        return Double.parseDouble(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return true;
    }
}
