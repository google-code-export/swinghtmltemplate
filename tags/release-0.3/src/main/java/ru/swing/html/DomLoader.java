package ru.swing.html;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import ru.swing.html.tags.*;
import ru.swing.html.tags.Attribute;
import ru.swing.html.tags.Object;

import java.io.IOException;
import java.io.InputStream;

public class DomLoader {

    private static LibraryRegistry registry = new LibraryRegistry();
    static {
        registerLibrary(null, new HtmlTagFactory());
        registerLibrary("http://www.w3.org/1999/xhtml", new HtmlTagFactory());
        registerLibrary("http://www.oracle.com/swing", new SwingTagFactory());
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

        model.fillIds();
        return model;
    }

    /**
     * Преобразует jdom-элемент в тег.
     * @param element jdom-элемент
     * @param tag тег
     */
    private static void parseElement(Element element, Tag tag) {
        //присваиваем имя тега
        tag.setName(element.getName().toLowerCase());
        //присваиваем атрибуты
        for (java.lang.Object o : element.getAttributes()) {
            org.jdom.Attribute a = (org.jdom.Attribute) o;
            tag.setAttribute(a.getName().toLowerCase(), a.getValue());
        }

        //присваиваем содержимое тега
        tag.setContent(element.getText());

        //рекурсивно обрабатываем дочерние теги
        for (java.lang.Object o : element.getChildren()) {
            Element child = (Element) o;
            Tag childTag = registry.getTagFactory(child.getNamespaceURI()).createTag(child);
            tag.addChild(childTag);
            parseElement(child, childTag);
        }
    }


}
