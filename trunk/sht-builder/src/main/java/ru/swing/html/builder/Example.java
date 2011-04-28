package ru.swing.html.builder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Javabean, holding info of the examples.
 */
public class Example {

    /**
     * The name of the example.
     */
    private String name;

    /**
     * The path to the html of the example.
     */
    private String source;

    /**
     * The path to the groovy code of the example.
     */
    private String code;

    /**
     * The path, where the example must be located in the examples tree.
     */
    private String[] path;

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Example() {
    }

    public Example(String name, String source, String code, String[] path) {
        this.name = name;
        this.source = source;
        this.path = path;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, name);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        String old = this.source;
        this.source = source;
        pcs.firePropertyChange("source", old, source);
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        String[] old = this.path;
        this.path = path;
        pcs.firePropertyChange("path", old, path);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        String old = this.code;
        this.code = code;
        pcs.firePropertyChange("code", old, code);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return name;
    }
}
