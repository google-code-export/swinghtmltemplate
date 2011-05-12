package ru.swing.html.configuration;

import ru.swing.html.layout.*;
import ru.swing.html.tags.Tag;

/**
 * This service is responsible for creating the layout manager support for the tag.
 */
public interface LayoutService {

    public void addLayoutManagerSupport(String layoutId, Class<? extends LayoutManagerSupport> layoutManagerSupportClass);
    public LayoutManagerSupport createLayout(Tag tag);

}
