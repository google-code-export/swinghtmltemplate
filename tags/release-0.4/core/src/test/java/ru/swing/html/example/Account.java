package ru.swing.html.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Test java bean for login form.
 */
public class Account {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String name;
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", old, name);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String old = this.password;
        this.password = password;
        propertyChangeSupport.firePropertyChange("password", old, password);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
