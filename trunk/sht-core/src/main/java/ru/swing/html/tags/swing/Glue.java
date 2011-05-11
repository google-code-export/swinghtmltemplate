package ru.swing.html.tags.swing;

import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 * Tag is converted to the component, that is returned from
 * <pre>
 * javax.swing.Box.createHorizontalGlue();
 * </pre>
 * for <pre>type="horizontal"</pre>, or
 * <pre>
 * javax.swing.Box.createVerticalGlue();
 * </pre>
 * for <pre>type="vertical"</pre>. Default `type` value is `vertical`.
 *
 * <p>Used within tags with BoxLayout layout</p>
 *
 * <h2>Example:</h2>
 * <pre>
 * &lt;c:glue type="horizontal"/>
 * </pre>
 *
 */
public class Glue extends Tag {

    @Override
    public JComponent createComponent() {
        if ("horizontal".equals(getType())) {
            return (JComponent) Box.createHorizontalGlue();
        }
        else {
            return (JComponent) Box.createVerticalGlue();
        }
    }

    @Override
    public void handleLayout() {
    }

}
