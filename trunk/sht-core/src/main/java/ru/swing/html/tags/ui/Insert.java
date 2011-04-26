package ru.swing.html.tags.ui;

import ru.swing.html.tags.Tag;

/**
 * Inserts content into a template. That content is defined (with the ui:define tag) in a ui:composition.
 *
 * @see Composition
 * @see Define
 */
public class Insert extends Tag {

    private String snippetName;

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("name".equals(name)) {
            setSnippetName(value);
        }
    }



    public String getSnippetName() {
        return snippetName;
    }

    public void setSnippetName(String snippetName) {
        this.snippetName = snippetName;
    }

}
