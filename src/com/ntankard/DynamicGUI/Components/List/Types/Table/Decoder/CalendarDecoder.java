package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarDecoder implements Decoder {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm");
        return dateFormat.format(((Calendar) value).getTime());
    }
}
