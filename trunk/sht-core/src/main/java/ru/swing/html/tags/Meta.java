package ru.swing.html.tags;

import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.util.Map;

/**
 * For &lt;meta> tag
 */
public class Meta extends Tag {

    private String metaName;
    private String metaContent;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("name".equals(name)) {
            setMetaName(value);
        }
        else if ("content".equals(name)) {
            setMetaContent(value);
        }
    }

    @Override
    public void applyAttributes(JComponent component) {
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public String getMetaContent() {
        return metaContent;
    }

    public void setMetaContent(String metaContent) {
        this.metaContent = metaContent;
    }
}
