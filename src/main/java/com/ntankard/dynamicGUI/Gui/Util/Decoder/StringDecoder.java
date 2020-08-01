package com.ntankard.dynamicGUI.Gui.Util.Decoder;

public class StringDecoder extends Decoder<String> {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(String value, Object rowObject) {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String encode(String value) {
        return value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return true;
    }
}
