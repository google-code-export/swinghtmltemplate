package ru.swing.html.xhtmlrenderer;

import org.apache.commons.lang.StringUtils;
import org.jdom.*;
import org.jdom.output.DOMOutputter;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.SwingReplacedElementFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.ELUtils;
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


        //replace ReplacedElementFactory so we can inject our components
        panel.getSharedContext().setReplacedElementFactory(new SHTReplacedElementFactory(this));


        //here we create new Document for XHTMLPanel. It will contain
        //<html>
        // <head>
        //   <style>
        //     #all global styles from dom model are inserted here#
        //   </style>
        // </head>
        // <body>
        //   #all jdom elements from original document are inserted here#
        // </body>
        //</html>
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


/*
        Element body = (Element) getJdomElement().clone();
        //forse 'display:block' for swing elements
        checkElement(body);
        html.addContent(body);
*/

        Element body = new Element("body", "http://www.w3.org/1999/xhtml");
        body.setText(getContent());
        html.addContent(body);



        convert(body, this);

        DOMOutputter outputter = new DOMOutputter();
        try {
            org.w3c.dom.Document document = outputter.output(doc);
            panel.setDocument(document);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts <code>tag</code> into jdom element and appends it to <code>root</code>.
     * Recursivelly invokes itself for every <code>tag</code> child passing created element as new <code>root</code>.
     * @param root root jdom elements.
     * @param tag tag to convert
     */
    private void convert(Element root, Tag tag) {
        for (Object c : tag.getContentChildren()) {
            if (c instanceof Tag) {
                Element el = tagToElement((Tag) c);
                root.addContent(el);
                convert(el, (Tag) c);
            }
            else {
                String textContent = c.toString();
                textContent = ELUtils.parseStringValue(textContent, tag.getModelElements());
                root.addContent(new Text(textContent));
            }
        }
    }


    /**
     * Checks jdom element and its children (recursivelly) if it contain "renderer='swing'" attribute value. If it does,
     * when method forces 'style' attribute to have css attr 'display:block'.
     * @param element jdom element
     */
    private void checkElement(Element element) {
        if (element.getAttribute("renderer")!=null && "swing".equals(element.getAttribute("renderer").getValue())) {
            String original = element.getAttribute("style")!=null ? element.getAttribute("style").getValue() : "";
            String modifiedStyle = Utils.replaceDisplay(original, "display", "block");
            element.setAttribute("style", modifiedStyle);
        }

        for (Object c : element.getChildren()) {
            if (c instanceof Element) {
                checkElement((Element) c);
            }
        }
    }


    /**
     * Converts tag to jdom element.
     * @param tag original tag to convert
     * @return created element
     */
    private Element tagToElement(Tag tag) {

        Element el = new Element(tag.getName(), tag.getPrefix(), tag.getNamespace());

        for (String attrName : tag.getAttributes().keySet()) {
            String attribute = tag.getAttribute(attrName);
            if (attribute!=null) {
                el.setAttribute(attrName, attribute);
            }
        }
        //el.setText(tag.getContent());

        //force 'display:block' so SHTReplacedElementFactory
        //could replace this element
        if ("swing".equals(tag.getAttribute("renderer"))) {
            if (tag.getComponent()==null) {
                DomConverter.convertComponent(tag);
                JComponent component = tag.getComponentWrapper();
                component.setSize(component.getPreferredSize());
            }
            String original = tag.getAttribute("style");
            String modifiedStyle = Utils.replaceDisplay(original, "display", "block");
            el.setAttribute("style", modifiedStyle);
        }

        return el;
    }


}
