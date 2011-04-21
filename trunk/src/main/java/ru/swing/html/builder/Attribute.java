package ru.swing.html.builder;

import java.beans.PropertyChangeSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 21.04.11
 * Time: 17:17
 */
public class Attribute {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        String old = this.value;
        this.value = value;
        pcs.firePropertyChange("value", old, value);
    }
}
