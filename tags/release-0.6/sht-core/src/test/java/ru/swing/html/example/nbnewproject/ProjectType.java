package ru.swing.html.example.nbnewproject;

import java.util.Arrays;

public class ProjectType extends AbstractJavaBean {

    private String name;
    private String description = "";
    private String icon;
    private String[] path;

    public ProjectType() {
    }

    public ProjectType(String name, String icon, String description, String[] path) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange("name", old, name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String old = this.description;
        this.description = description;
        firePropertyChange("description", old, description);
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean fitsPath(String[] path) {
        boolean fits = true;
        if (path.length<=0) {
            return fits;
        }
        else if (path.length>=this.path.length) {
            return false;
        }

        for (int i=0; i<path.length && fits; i++) {
            if (!path[i].equals(this.path[i])) {
                fits = false;
            }
        }
        return fits;
    }

    @Override
    public String toString() {
        return "ProjectType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", path=" + (path == null ? null : Arrays.asList(path)) +
                '}';
    }
}
