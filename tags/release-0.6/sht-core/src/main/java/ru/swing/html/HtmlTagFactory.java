package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.configuration.LayoutService;
import ru.swing.html.layout.*;
import ru.swing.html.tags.*;
import ru.swing.html.tags.Object;
import ru.swing.html.tags.swing.*;

/**
 * Factory for default html tags, like &lt;div&gt;.
 */
public class HtmlTagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());

    public Tag createTag(String name) {
        if ("attribute".equals(name)) {
            logger.warn("attribute tag moved to 'http://www.oracle.com/swing' namespace");
            return new Attribute();
        }
        else if ("body".equals(name)) {
            return new Body();
        }
        else if ("div".equals(name)) {
            return new Div();
        }
        else if ("img".equals(name)) {
            return new Img();
        }
        else if ("form".equals(name)) {
            return new Form();
        }
        else if ("hr".equals(name)) {
            return new Hr();
        }
        else if ("input".equals(name)) {
            return new Input();
        }
        else if ("label".equals(name)) {
            return new Label();
        }
        else if ("meta".equals(name)) {
            return new Meta();
        }
        else if ("object".equals(name)) {
            return new Object();
        }
        else if ("p".equals(name)) {
            return new P();
        }
        else if ("table".equals(name)) {
            return new Table();
        }
        else if ("textarea".equals(name)) {
            return new TextArea();
        }
        else if ("tr".equals(name)) {
            return new Tr();
        }
        else if ("scroll".equals(name)) {
            logger.warn("scroll tag moved to 'http://www.oracle.com/swing' namespace");
            return new ScrollPane();
        }
        else if ("span".equals(name)) {
            return new Span();
        }
        else if ("split".equals(name)) {
            logger.warn("split tag moved to 'http://www.oracle.com/swing' namespace");
            return new SplitPane();
        }
        else if ("strut".equals(name)) {
            logger.warn("strut tag moved to 'http://www.oracle.com/swing' namespace");
            return new Strut();
        }
        else if ("tabs".equals(name)) {
            logger.warn("tabs tag moved to 'http://www.oracle.com/swing' namespace");
            return new Tabs();
        }
        else {
            return new Tag();
        }
    }

    public void libraryLoaded(DomModel model) {
    }
}
