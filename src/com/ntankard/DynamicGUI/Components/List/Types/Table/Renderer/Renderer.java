package com.ntankard.DynamicGUI.Components.List.Types.Table.Renderer;

import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.Decoder;

import javax.swing.table.DefaultTableCellRenderer;

public class Renderer extends DefaultTableCellRenderer {

    /**
     * The decoder used to get the display string
     */
    private Decoder decoder = null;

    /**
     * Set the decoder used to get the display string
     *
     * @param decoder the decoder used to get the display string
     */
    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void setValue(Object value) {
        if (decoder != null) {
            super.setValue(decoder.decode(value));
        } else {
            super.setValue(value);
        }
    }
}
