package ru.swing.html.tags;

import javax.swing.*;

/**
 * Tag is converted to the component which classname is specified with `classid` attribute.
 * Example:
 * <pre>
 * &lt;object classid="javax.swing.JButton" text="OK"/>
 * </pre>
 */
public class Object extends Tag {

    @Override
    public JComponent createComponent() {
        String classname = getAttribute("classid");
        JComponent c;
        Class cl;
        try {
            cl = Class.forName(classname);
            c = (JComponent) cl.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't create component for class "+classname+": "+e.getMessage());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Can't create component for class "+classname+": "+e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't create component for class "+classname+": "+e.getMessage());
        }

        setComponent(c);
        return c;

    }
}
