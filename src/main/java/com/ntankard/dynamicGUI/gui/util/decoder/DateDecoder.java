package com.ntankard.dynamicGUI.gui.util.decoder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDecoder extends Decoder<Date> {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Date value, Object rowObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return dateFormat.format(value.getTime());
    }
}
