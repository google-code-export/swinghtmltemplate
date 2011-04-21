package ru.swing.html.tags;

import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.util.Map;

/**
 * <p>
 * Child component for DataTable tag. It describes a data to display in this column.
 * </p>
 * <p>
 *     Binding for el is done using jsr-295.
 * </p>
 * <p>
 * Possible attributes:
 * </p>
 * <ul>
 * <li>value - el to dataTable's row bean property.
 * <li>title - column title
 * <li>readonly - is this column is editable. By default column is readonly.
 * <li>editor - el to dom-model-element, holding table editor for this column. If specified, fas more priority than 'editorClass'.
 * <li>editorClass - classname for table editor for this column. Must have default constructor.
 * <li>renderer - el to dom-model-element, holding table renderer for this column. If specified, fas more priority than 'rendererClass'.
 * <li>rendererClass - classname for table renderer for this column. Must have default constructor.
 * </ul>
 * @see DataTable
 */
public class Column extends Tag {

    private String value;
    private String title;
    private boolean editable;
    private String editor;
    private String editorClass;
    private String renderer;
    private String rendererClass;

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
        else if ("editorClass".equals(name)) {
            setEditorClass(value);
        }
        else if ("renderer".equals(name)) {
            setRenderer(value);
        }
        else if ("rendererClass".equals(name)) {
            setRendererClass(value);
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

    /**
     * Returns EL expression for renderer for this column.
     * @return
     */
    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getRendererClass() {
        return rendererClass;
    }

    public void setRendererClass(String rendererClass) {
        this.rendererClass = rendererClass;
    }

    public String getEditorClass() {
        return editorClass;
    }

    public void setEditorClass(String editorClass) {
        this.editorClass = editorClass;
    }
}
