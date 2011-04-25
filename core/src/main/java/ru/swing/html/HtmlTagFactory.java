package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import ru.swing.html.tags.*;
import ru.swing.html.tags.Object;
import ru.swing.html.tags.swing.*;

/**
 * Factory for default html tags, like &lt;div&gt;.
 */
public class HtmlTagFactory implements TagFactory {

    private Log logger = LogFactory.getLog(getClass());

    public Tag createTag(Element element) {
        if ("attribute".equals(element.getName())) {
            logger.warn("attribute tag moved to 'http://www.oracle.com/swing' namespace");
            return new Attribute();
        }
        else if ("body".equals(element.getName())) {
            return new Body();
        }
        else if ("div".equals(element.getName())) {
            return new Div();
        }
        else if ("img".equals(element.getName())) {
            return new Img();
        }
        else if ("form".equals(element.getName())) {
            return new Form();
        }
        else if ("hr".equals(element.getName())) {
            return new Hr();
        }
        else if ("input".equals(element.getName())) {
            return new Input();
        }
        else if ("label".equals(element.getName())) {
            return new Label();
        }
        else if ("object".equals(element.getName())) {
            return new Object();
        }
        else if ("p".equals(element.getName())) {
            return new P();
        }
        else if ("table".equals(element.getName())) {
            return new Table();
        }
        else if ("textarea".equals(element.getName())) {
            return new TextArea();
        }
        else if ("scroll".equals(element.getName())) {
            logger.warn("scroll tag moved to 'http://www.oracle.com/swing' namespace");
            return new ScrollPane();
        }
        else if ("span".equals(element.getName())) {
            return new Span();
        }
        else if ("split".equals(element.getName())) {
            logger.warn("split tag moved to 'http://www.oracle.com/swing' namespace");
            return new SplitPane();
        }
        else if ("strut".equals(element.getName())) {
            logger.warn("strut tag moved to 'http://www.oracle.com/swing' namespace");
            return new Strut();
        }
        else if ("tabs".equals(element.getName())) {
            logger.warn("tabs tag moved to 'http://www.oracle.com/swing' namespace");
            return new Tabs();
        }
        else {
            return new Tag();
        }
    }
}
