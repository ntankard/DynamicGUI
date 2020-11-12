package com.ntankard.dynamicGUI.gui.util.decoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarDecoder extends Decoder<Calendar> {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Calendar value, Object rowObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm");
        return dateFormat.format(value.getTime());
    }
}
