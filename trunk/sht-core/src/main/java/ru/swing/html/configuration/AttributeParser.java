package ru.swing.html.configuration;

import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 * Special service for parsing common tag attributes and apply the values to the component
 */
public interface AttributeParser {

    /**
     * Applies attribute from the specified tag with the attrName to the component
     * @param tag tag, holding the value of the attribute
     * @param component component, attribute will be applied to it
     * @param attrName the name of the attribute
     * @see Tag#applyAttribute(javax.swing.JComponent, String)
     */
    public void applyAttribute(Tag tag, JComponent component, String attrName);
}
