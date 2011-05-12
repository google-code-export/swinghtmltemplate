package ru.swing.html.xhtmlrenderer;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.DOMOutputter;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.SwingReplacedElementFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.TagVisitor;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class XhtmlRenderer extends Tag {


    @Override
    public JComponent createComponent() {
        return new XHTMLPanel();
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        XHTMLPanel panel = (XHTMLPanel) getComponent();


        panel.getSharedContext().setReplacedElementFactory(new SHTReplacedElementFactory(this));


        Element html = new Element("html", "http://www.w3.org/1999/xhtml");
        html.addNamespaceDeclaration(Namespace.getNamespace("j", "http://www.oracle.com/swing"));
        final Document doc = new Document(html);

        Element head = new Element("head", "http://www.w3.org/1999/xhtml");
        Element style = new Element("style", "http://www.w3.org/1999/xhtml");
        String css = StringUtils.join(getModel().getGlobalStyles().iterator(), " ");
        style.setText(css);
        style.setAttribute("type", "text/css");
        head.addContent(style);
        html.addContent(head);


        Element body = new Element("body", "http://www.w3.org/1999/xhtml");
        body.setText(getContent());
        html.addContent(body);

        for (Tag c : getChildren()) {
            convert(body, c);
        }

        DOMOutputter outputter = new DOMOutputter();
        try {
            org.w3c.dom.Document document = outputter.output(doc);
            panel.setDocument(document);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    private void convert(Element root, Tag child) {
        Element el = tagToElement(root, child);
        for (Tag c : child.getChildren()) {
            convert(el, c);
        }
    }


    /**
     * Converts tag to jdom element.
     * @param parent parent element, new element will be added to this
     * @param tag original tag to convert
     * @return created element
     */
    private Element tagToElement(Element parent, Tag tag) {

        Element el = new Element(tag.getName(), tag.getPrefix(), tag.getNamespace());

        for (String attrName : tag.getAttributes().keySet()) {
            String attribute = tag.getAttribute(attrName);
            if (attribute!=null) {
                el.setAttribute(attrName, attribute);
            }
        }
        el.setText(tag.getContent());

        //force 'display:block' so SHTReplacedElementFactory
        //could replace this element
        if ("swing".equals(tag.getAttribute("renderer"))) {
            String original = tag.getAttribute("style");
            String modifiedStyle = replaceDisplay(original, "display", "block");
            el.setAttribute("style", modifiedStyle);
        }

        parent.addContent(el);
        return el;
    }


    /**
     * Modifies css style string so 'attrName' attribute is set to 'attrValue'. If there is no
     * such attribute, it is added.
     * @param originalStyle original string with css style
     * @param attrName name of the attribute to add/modify
     * @param attrName value of the attribute
     * @return modified css style with added or replaced attribute
     */
    public String replaceDisplay(String originalStyle, String attrName, String attrValue) {
        Map<String, String> styles = StyleParser.extractStyles(originalStyle);
        styles.put(attrName, attrValue);
        StringBuilder modifiedStyle = new StringBuilder();
        for (String key : styles.keySet()) {
            modifiedStyle.append(key).append(":").append(styles.get(key)).append(";");
        }
        return modifiedStyle.toString();
    }
}
