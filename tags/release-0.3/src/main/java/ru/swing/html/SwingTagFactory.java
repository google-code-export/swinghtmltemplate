package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.tags.*;

/**
 * Factory for swing tags, like &lt;strut&gt;.
 */
public class SwingTagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());

    public Tag createTag(Element element) {
        if ("attribute".equals(element.getName())) {
            return new Attribute();
        }
        else if ("editorPane".equals(element.getName())) {
            return new EditorPane();
        }
        else if ("glue".equals(element.getName())) {
            return new Glue();
        }
        else if ("list".equals(element.getName())) {
            return new List();
        }
        else if ("scroll".equals(element.getName())) {
            return new ScrollPane();
        }
        else if ("split".equals(element.getName())) {
            return new SplitPane();
        }
        else if ("strut".equals(element.getName())) {
            return new Strut();
        }
        else if ("tabs".equals(element.getName())) {
            return new Tabs();
        }
        else if ("spinner".equals(element.getName())) {
            return new Spinner();
        }
        else if ("dataTable".equals(element.getName())) {
            return new DataTable();
        }
        else if ("formTable".equals(element.getName())) {
            return new FormTable();
        }
        else if ("column".equals(element.getName())) {
            return new Column();
        }
        else if ("combobox".equals(element.getName())) {
            return new Combobox();
        }
        else if ("selectItems".equals(element.getName())) {
            return new SelectItems();
        }
        else if ("tree".equals(element.getName())) {
            return new Tree();
        }
        else {
            logger.warn("Unknown tag: "+element.getNamespacePrefix()+":"+element.getName());
            return null;
        }
    }
}
