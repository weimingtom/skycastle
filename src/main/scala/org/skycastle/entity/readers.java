package org.skycastle.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Comma separated list of the roles that are allowed to read a property.
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD )
public @interface readers
{
    /**
     * @return Comma separated list of the roles that are allowed to read a property.
     */
    String value() default "";
}
