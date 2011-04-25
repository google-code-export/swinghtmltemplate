package ru.swing.html.example.nbnewproject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * java bean with property change support
 */
public class AbstractJavaBean {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }
}
