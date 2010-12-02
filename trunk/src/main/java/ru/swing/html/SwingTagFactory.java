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
        if ("glue".equals(element.getName())) {
            return new Glue();
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
        else {
            logger.warn("Unknown tag: "+element.getNamespacePrefix()+":"+element.getName());
            return null;
        }
    }
}
