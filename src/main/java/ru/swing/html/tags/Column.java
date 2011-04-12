package ru.swing.html.tags;

import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 12.04.11
 * Time: 16:13
 */
public class Column extends Tag {

    private String value;
    private String title;
    private boolean editable;
    private String editor;
    private String renderer;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void handleLayout() {
    }




    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("value".equals(name)) {
            setValue(value);
        }
        else if ("title".equals(name)) {
            setTitle(value);
        }
        else if ("readonly".equals(name)) {
            boolean editable = !Boolean.parseBoolean(name);
            setEditable(editable);
        }
        else if ("editor".equals(name)) {
            setEditor(value);
        }
        else if ("renderer".equals(name)) {
            setRenderer(value);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }
}
