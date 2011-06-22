package ru.swing.html.tags.ui;

import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

/**
 * <p>
 * This tag is used to provide condition to &lt;choose> tag. Condition is set with
 * 'test' attribute in the form of EL.
 *
 * <h2>Example:</h2>
 * <pre>
 *     &lt;when test='${someEL}'>
 *        &lt;p>child tag&lt;/p>
 *     &lt;/when>
 * </pre>
 *
 * @see Choose
 * @see Otherwise
 */
public class When extends Tag {

    private String test;

    @Override
    public void setAttribute(String name, String value) {
        if ("test".equals(name)) {
            setTest(value);
        }
        super.setAttribute(name, value);
    }

    @Override
    public void handleLayout() {
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
