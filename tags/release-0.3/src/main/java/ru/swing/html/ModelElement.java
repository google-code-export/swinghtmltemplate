package ru.swing.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks property as model element.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelElement {

    /**
     * The key, under which the element will be added to the dom model.
     * @return thekey
     */
    public String value();

}
