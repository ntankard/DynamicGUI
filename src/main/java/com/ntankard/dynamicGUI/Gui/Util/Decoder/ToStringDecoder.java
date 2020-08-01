package com.ntankard.dynamicGUI.Gui.Util.Decoder;

public class ToStringDecoder extends Decoder<Object> {

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
        return false;
    }
}
