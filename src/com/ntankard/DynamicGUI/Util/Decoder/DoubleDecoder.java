package com.ntankard.DynamicGUI.Util.Decoder;

import java.text.DecimalFormat;

public class DoubleDecoder extends Decoder<Double> {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Double value, Object rowObject) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        return df2.format(value);
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
