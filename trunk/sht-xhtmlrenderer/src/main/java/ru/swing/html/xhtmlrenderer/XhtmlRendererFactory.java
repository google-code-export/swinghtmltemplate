package ru.swing.html.xhtmlrenderer;

import ru.swing.html.DomModel;
import ru.swing.html.TagFactory;
import ru.swing.html.tags.Tag;

public class XhtmlRendererFactory implements TagFactory {

    public Tag createTag(String name) {
        if ("xhtmlpanel".equals(name)) {
            return new XhtmlRenderer();
        }
        return null;
    }

    public void libraryLoaded(DomModel model) {
    }
}
