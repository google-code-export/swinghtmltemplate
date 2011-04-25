package ru.swing.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация указывает что поле, помеченное данной аннотацией, будет проинициализировано
 * компонентом, тег которого имеет идентификатор, указанный в значении value().
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {

    /**
     * Идентификатор тега, с которым будет произведена привязка поля.
     * @return идентификатор тега
     */
    public String value();

}
