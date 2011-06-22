package ru.swing.html;

import org.jdom.Element;
import ru.swing.html.tags.Tag;

/**
 * TagFactory
 */
public interface TagFactory {

    /**
     * Tag factory. Creates tag by it's name.
     * @param name tag's name
     * @return tag
     */
    public Tag createTag(String name);

    /**
     * This callback method is called in DomLoader after all libraries are loaded.
     * @param model dom model, holding libraries
     */
    void libraryLoaded(DomModel model);
}
