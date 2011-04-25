package ru.swing.html;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import ru.swing.html.tags.*;
import ru.swing.html.tags.swing.Attribute;
import ru.swing.html.tags.Object;

import java.io.IOException;
import java.io.InputStream;

public class DomLoader {

    private static LibraryRegistry registry = new LibraryRegistry();
    static {
        registerLibrary(null, new HtmlTagFactory());
        registerLibrary("http://www.w3.org/1999/xhtml", new HtmlTagFactory());
        registerLibrary("http://www.oracle.com/swing", new SwingTagFactory());
        registerLibrary("http://swinghtmltemplate.googlecode.com/ui", new UITagFactory());
    }

    
    public static void registerLibrary(String namespace, TagFactory tagFactory) {
        registry.registerLibrary(namespace, tagFactory);
    }

    /**
     * Загружает dom-модель html-документа.
     * @param in поток, из которого происходит считывание html-документа
     * @return dom-модель документа
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel loadModel(InputStream in) throws JDOMException, IOException {

        DomModel model = new DomModel();

        SAXBuilder builder = new SAXBuilder();
        //disable validation and loading of external dtd
        //http://www.jdom.org/docs/faq.html#a0350
        builder.setValidation(false);
        builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = builder.build(in);

        Element root = doc.getRootElement();
        Tag rootTag = registry.getTagFactory(root.getNamespaceURI()).createTag(root);
        rootTag.setModel(model);
        model.setRootTag(rootTag);
        parseElement(root, rootTag);
        rootTag.afterChildElementsConverted();

        //child tag may substitute model, we must reassign it to all tags
        model = rootTag.getModel();
        for (Tag t : model.query("*")) {
            t.setModel(model);
        }

        model.fillIds();
        return model;
    }

    /**
     * Converts jdom-element to the tag
     * @param element jdom-element
     * @param tag tag
     */
    private static void parseElement(Element element, Tag tag) {
        //assign tag's name
        tag.setName(element.getName().toLowerCase());
        //assign attributes
        for (java.lang.Object o : element.getAttributes()) {
            org.jdom.Attribute a = (org.jdom.Attribute) o;
            tag.setAttribute(a.getName().toLowerCase(), a.getValue());
        }

        //assign tag's content
        tag.setContent(element.getText());

        //recursively convert children
        for (java.lang.Object o : element.getChildren()) {
            Element child = (Element) o;
            Tag childTag = registry.getTagFactory(child.getNamespaceURI()).createTag(child);
            tag.addChild(childTag);
            parseElement(child, childTag);
            //child tag may substitute model
            tag.setModel(childTag.getModel());
        }

        tag.afterChildElementsConverted();

    }


}
