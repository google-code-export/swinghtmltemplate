package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.ui.Include;

/**
 * Factory for 'http://swinghtmltemplate.googlecode.com/ui' namespace.
 */
public class UITagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());
   
    public Tag createTag(Element element) {
        if ("include".equals(element.getName())) {
            return new Include();
        }
        else {
            logger.warn("Unknown tag: "+element.getNamespacePrefix()+":"+element.getName());
            return null;
        }
    }
}
