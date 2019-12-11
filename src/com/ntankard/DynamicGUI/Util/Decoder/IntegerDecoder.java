package com.ntankard.DynamicGUI.Util.Decoder;

public class IntegerDecoder extends Decoder<Integer> {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Integer value, Object rowObject) {
        return value.toString();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Integer encode(String value) {
        return Integer.parseInt(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return true;
    }
}
