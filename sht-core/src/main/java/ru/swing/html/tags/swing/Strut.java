package ru.swing.html.tags.swing;

import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 Tag is converted to the component, that is returned from
 <pre>
 javax.swing.Box.createHorizontalStrut(w)
 </pre>
 for `type="horizontal"`, or
 <pre>
 javax.swing.Box.createVerticalStrut(h)
 </pre>
 for `type="vertical"`. `type` has default value `vertical`.

 <p>Used within tags with `BoxLayout` layout</p>

 <p>`w` and `h` - are values of attributes `width` и `height` accordingly.</p>

 <h2>Example:</h2>
 <pre>
 &lt;c:strut type="horizontal" style="width: 12"/>
 &lt;c:strut type="vertical" height="12"/>
 </pre>
 */
public class Strut extends Tag {

    @Override
    public JComponent createComponent() {
        if ("horizontal".equals(getType())) {
            return (JComponent) Box.createHorizontalStrut(new Integer(getWidth()));
        }
        else {
            return (JComponent) Box.createVerticalStrut(new Integer(getHeight()));
        }
    }

    @Override
    public void handleLayout() {
    }


}
