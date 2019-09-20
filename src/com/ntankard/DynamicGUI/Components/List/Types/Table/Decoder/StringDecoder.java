package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

public class StringDecoder extends Decoder {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value, Object rowObject) {
        return value.toString();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return true;
    }
}
