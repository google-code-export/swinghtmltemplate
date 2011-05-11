package ru.swing.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation tells that the field, marked with it, will be initialized with the component,
 * whose tag has id sprcified in "value()"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

    /**
     * The tag id, with which the linking will be done
     * @return id
     */
    public String value();

}
