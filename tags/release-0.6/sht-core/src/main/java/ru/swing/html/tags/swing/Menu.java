package ru.swing.html.tags.swing;

import ru.swing.html.DomConverter;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Hr;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Is converted to JMenu
 */
public class Menu extends Tag {


    private JComponent popupTarget;
    private String title;

    @Override
    public JComponent createComponent() {
        return new JMenu();
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        JMenu component = (JMenu) getComponent();
        for (Tag childTag : getChildren()) {
            if (childTag instanceof MenuItem) {
                JMenuItem child = (JMenuItem) DomConverter.convertComponent(childTag, substitutions);
                component.add(child);
            } else if (childTag instanceof Menu) {
                JComponent child = DomConverter.convertComponent(childTag, substitutions);
                component.add(child);
            } else if (childTag instanceof Hr) {
                component.addSeparator();
            }
        }

    }


    @Override
    public void setAttribute(String name, String value) {
        if ("title".equals(name)) {
            setTitle(value);
        }
        super.setAttribute(name, value);
    }

    @Override
    public void applyAttribute(JComponent component, String attrName) {
        JMenu menu = (JMenu) component;
        if ("title".equals(attrName)) {
            menu.setText(getTitle());
        }
        else {
            super.applyAttribute(component, attrName);
        }
    }

    public JComponent getPopupTarget() {
        return popupTarget;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPopupTarget(JComponent popupTarget) {
        this.popupTarget = popupTarget;
        for (Tag child : getChildren()) {
            if (child instanceof Menu) {
                ((Menu) child).setPopupTarget(popupTarget);
            }
            else if (child instanceof MenuItem) {
                ((MenuItem) child).setPopupTarget(popupTarget);
            }
        }
    }
}
