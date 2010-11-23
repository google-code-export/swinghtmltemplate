package ru.swing.html;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import ru.swing.html.tags.*;
import ru.swing.html.tags.Object;

import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 10:48:52
 * </pre>
 */
public class DomLoader {

    public static DomModel loadModel(InputStream in) throws JDOMException, IOException {

        DomModel model = new DomModel();

        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);

        Element root = doc.getRootElement();
        Tag rootTag = createTag(root);
        rootTag.setModel(model);
        model.setRootTag(rootTag);
        parseElement(root, rootTag);

        model.fillIds();
        return model;
    }

    private static void parseElement(Element element, Tag tag) {
        tag.setName(element.getName());
        for (java.lang.Object o : element.getAttributes()) {
            org.jdom.Attribute a = (org.jdom.Attribute) o;
            tag.setAttribute(a.getName(), a.getValue());
        }

        tag.setContent(element.getText());

        for (java.lang.Object o : element.getChildren()) {
            Element child = (Element) o;
            Tag childTag = createTag(child);
            childTag.setModel(tag.getModel());
            childTag.setParent(tag);
            tag.getChildren().add(childTag);
            parseElement(child, childTag);
        }
    }

    private static Tag createTag(Element element) {
        if ("table".equals(element.getName())) {
            return new Table();
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
        else if ("glue".equals(element.getName())) {
            return new Glue();
        }
        else if ("input".equals(element.getName())) {
            return new Input();
        }
        else if ("object".equals(element.getName())) {
            return new Object();
        }
        else if ("textarea".equals(element.getName())) {
            return new TextArea();
        }
        else if ("scroll".equals(element.getName())) {
            return new ScrollPane();
        }
        else if ("span".equals(element.getName())) {
            return new Span();
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
        else if ("p".equals(element.getName())) {
            return new P();
        }
        else {
            return new Tag();
        }
    }

}
