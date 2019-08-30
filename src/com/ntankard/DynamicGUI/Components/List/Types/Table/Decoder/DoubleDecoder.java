package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.DecimalFormat;

public class DoubleDecoder implements Decoder {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        return df2.format(value);
    }
}
