package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.ui.*;

/**
 * Factory for 'http://swinghtmltemplate.googlecode.com/ui' namespace.
 */
public class UITagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());
   
    public Tag createTag(String name) {
        if ("component".equals(name)) {
            return new Component();
        }
        else if ("composition".equals(name)) {
            return new Composition();
        }
        else if ("choose".equals(name)) {
            return new Choose();
        }
        else if ("define".equals(name)) {
            return new Define();
        }
        else if ("foreach".equalsIgnoreCase(name)) {
            return new ForEach();
        }
        else if ("if".equals(name)) {
            return new If();
        }
        else if ("include".equals(name)) {
            return new Include();
        }
        else if ("insert".equals(name)) {
            return new Insert();
        }
        else if ("loadBundle".equals(name)) {
            return new LoadBundle();
        }
        else if ("otherwise".equals(name)) {
            return new Otherwise();
        }
        else if ("set".equals(name)) {
            return new Set();
        }
        else if ("when".equals(name)) {
            return new When();
        }
        else {
            logger.warn("Unknown tag: "+name);
            return null;
        }
    }
}
