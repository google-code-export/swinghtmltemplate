package ru.swing.html.tags.ui;

import ru.swing.html.tags.Tag;

/**
 * The define tag defines content that is inserted into a page by a template.
 * Content defined by the define tag can be inserted into a page by using "insert".
 *
 * The name of the content of this tag can be assigned with "name" attribute.
 * @see Insert
 */
public class Define extends Tag {

    private String snippetName;

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("name".equals(name)) {
            setSnippetName(value);
        }
    }



    /**
     * Returns a name of the content inside a define tag. That name is used by corresponding "insert" tags
     * in a template that insert the named content into a page.
     * @return name
     */
    public String getSnippetName() {
        return snippetName;
    }

    /**
     * Assigns a name to the content inside a define tag. That name is used by corresponding "insert" tags
     * in a template that insert the named content into a page.
     * @param snippetName name
     */
    public void setSnippetName(String snippetName) {
        this.snippetName = snippetName;
    }
}
