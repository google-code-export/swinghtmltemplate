package ru.swing.html.example.createproject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
* Data model for "Create project" form.
*/
public class Model {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String name;
    private boolean useDefaultPath;
    private String path;
    private String runtime;
    private String version;
    private String configuration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUseDefaultPath() {
        return useDefaultPath;
    }

    public void setUseDefaultPath(boolean useDefaultPath) {
        this.useDefaultPath = useDefaultPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", useDefaultPath=" + useDefaultPath +
                ", path='" + path + '\'' +
                ", runtime='" + runtime + '\'' +
                ", version='" + version + '\'' +
                ", configuration='" + configuration + '\'' +
                '}';
    }
}
