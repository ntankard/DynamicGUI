package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.DecimalFormat;

public class DoubleDecoder extends Decoder {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value, Object rowObject) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        return df2.format(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return true;
    }
}
