package com.ntankard.DynamicGUI.Generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Nicholas on 15/07/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldGuiProperties {

    /**
     * Should a component be generated for this field?
     */
    boolean generate() default true;

    /**
     * Can this field be edited?
     */
    boolean editable() default true;
}
