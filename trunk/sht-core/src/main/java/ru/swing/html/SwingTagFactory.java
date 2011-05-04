package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.tags.*;
import ru.swing.html.tags.swing.*;

/**
 * Factory for swing tags, like &lt;strut&gt;.
 */
public class SwingTagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());

    public Tag createTag(String name) {
        if ("attribute".equals(name)) {
            return new Attribute();
        }
        else if ("editorPane".equals(name)) {
            return new EditorPane();
        }
        else if ("glue".equals(name)) {
            return new Glue();
        }
        else if ("list".equals(name)) {
            return new List();
        }
        else if ("scroll".equals(name)) {
            return new ScrollPane();
        }
        else if ("split".equals(name)) {
            return new SplitPane();
        }
        else if ("strut".equals(name)) {
            return new Strut();
        }
        else if ("tabs".equals(name)) {
            return new Tabs();
        }
        else if ("spinner".equals(name)) {
            return new Spinner();
        }
        else if ("slider".equals(name)) {
            return new Slider();
        }
        else if ("dataTable".equals(name)) {
            return new DataTable();
        }
        else if ("formTable".equals(name)) {
            return new FormTable();
        }
        else if ("column".equals(name)) {
            return new Column();
        }
        else if ("combobox".equals(name)) {
            return new Combobox();
        }
        else if ("selectItems".equals(name)) {
            return new SelectItems();
        }
        else if ("tree".equals(name)) {
            return new Tree();
        }
        else {
            logger.warn("Unknown tag: "+name);
            return null;
        }
    }
}
