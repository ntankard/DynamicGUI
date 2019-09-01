package com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarDecoder extends Decoder {

    /**
     * @inheritDoc
     */
    @Override
    public String decode(Object value, Object rowObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm");
        return dateFormat.format(((Calendar) value).getTime());
    }
}
