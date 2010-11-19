package ru.swing.html;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import ru.swing.html.tags.ScrollPane;
import ru.swing.html.tags.SplitPane;
import ru.swing.html.tags.Table;
import ru.swing.html.tags.Tag;

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
        model.setRootTag(rootTag);
        parseElement(root, rootTag);

        model.fillIds();
        return model;
    }

    private static void parseElement(Element element, Tag tag) {
        tag.setName(element.getName());
        for (Object o : element.getAttributes()) {
            org.jdom.Attribute a = (org.jdom.Attribute) o;
            tag.setAttribute(a.getName(), a.getValue());
        }

        tag.setContent(element.getText());

        for (Object o : element.getChildren()) {
            Element child = (Element) o;
            Tag childTag = createTag(child);
            childTag.setParent(tag);
            tag.getChildren().add(childTag);
            parseElement(child, childTag);
        }
    }

    private static Tag createTag(Element element) {
        if ("table".equals(element.getName())) {
            return new Table();
        }
        else if ("scroll".equals(element.getName())) {
            return new ScrollPane();
        }
        else if ("split".equals(element.getName())) {
            return new SplitPane();
        }
        else {
            return new Tag();
        }
    }

}
