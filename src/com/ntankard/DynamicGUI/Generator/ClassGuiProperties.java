package com.ntankard.DynamicGUI.Generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Nicholas on 15/07/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassGuiProperties {

    /** Should fields from the class this extends be included when generating a GUI? */
    boolean includeParent() default false;
}