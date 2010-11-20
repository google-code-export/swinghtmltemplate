package ru.swing.html.tags;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:57:11
 */
public class Component extends Tag {

    @Override
    public JComponent createComponent() {
        String classname = getType();
        JComponent c;
        Class cl;
        try {
            cl = Class.forName(classname);
            c = (JComponent) cl.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't create component for class "+classname);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Can't create component for class "+classname);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Can't create component for class "+classname);
        }

        setComponent(c);
        return c;

    }
}
