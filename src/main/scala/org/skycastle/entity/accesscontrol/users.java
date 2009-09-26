package org.skycastle.entity.accesscontrol;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Used to annotate action methods in an entity that can be called by other entities.
 *
 * Specifies which roles are allowed to call the action as a space separated string.
 *
 * @author Hans Haggstrom
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD )
public @interface users
{
    /**
     * @return Space separated list of role identifiers that are allowed to call the action method.
     */
    String value() default "";
}
