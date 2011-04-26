package ru.swing.html.mydoggy;

import ru.swing.html.tags.Tag;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 26.04.11
 * Time: 11:47
 */
public class ToolWindow extends Tag {

    private String title;
    private String icon;
    private String visible;

    @Override
    public void setAttribute(String name, String value) {
        if ("title".equals(name)) {
            setTitle(value);
        }
        else if ("icon".equals(name)) {
            setIcon(value);
        }
        else if ("visible".equals(name)) {
            setVisible(value);
        }
        else {
            super.setAttribute(name, value);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
}
