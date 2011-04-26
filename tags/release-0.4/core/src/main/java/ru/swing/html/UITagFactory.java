package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.ui.Composition;
import ru.swing.html.tags.ui.Define;
import ru.swing.html.tags.ui.Include;
import ru.swing.html.tags.ui.Insert;

/**
 * Factory for 'http://swinghtmltemplate.googlecode.com/ui' namespace.
 */
public class UITagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());
   
    public Tag createTag(Element element) {
        if ("composition".equals(element.getName())) {
            return new Composition();
        }
        else if ("define".equals(element.getName())) {
            return new Define();
        }
        else if ("include".equals(element.getName())) {
            return new Include();
        }
        else if ("insert".equals(element.getName())) {
            return new Insert();
        }
        else {
            logger.warn("Unknown tag: "+element.getNamespacePrefix()+":"+element.getName());
            return null;
        }
    }
}
