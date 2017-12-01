package com.ntankard.DynamicGUI.Components.List;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BoundArray_FieldProperties {

    /**
     * At what verbosity level should this field be displayed?
     */
    int verbosityLevel() default 0;

    /**
     * Should the content of this field be considers part of the parent object?
     */
    boolean partComposite() default false;
}
