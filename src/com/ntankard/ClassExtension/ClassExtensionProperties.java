package com.ntankard.ClassExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Nicholas on 15/07/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassExtensionProperties {

    /**
     * Should fields from the class this extends be included when generating a GUI?
     */
    boolean includeParent() default false;
}