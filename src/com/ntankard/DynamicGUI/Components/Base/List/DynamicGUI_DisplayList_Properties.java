package com.ntankard.DynamicGUI.Components.Base.List;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicGUI_DisplayList_Properties {

    int ALWAYS_DISPLAY = 0;
    int INFO_DISPLAY = 1;
    int DEBUG_DISPLAY = 2;
    int TRACE_DISPLAY = 3;

    /**
     * At what verbosity level should this field be displayed?
     */
    int verbosityLevel() default ALWAYS_DISPLAY;
}
