package ru.swing.html.mydoggy;

import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

/**
 * Tag for holding info on MyDoggy toolwindow. Do not produces any component.
 */
public class ToolWindow extends Tag {

    private String title;
    private String icon;
    private String visible;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        //the only possible child is handled by MainWindow
    }

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
