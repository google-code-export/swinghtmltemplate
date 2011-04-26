package ru.swing.html.builder;

import java.beans.PropertyChangeSupport;

/**
 * Java bean for editing attributes of the selected tag.
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
