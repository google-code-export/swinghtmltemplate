package ru.swing.html;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 17:48:02
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {

    public String value();

}
