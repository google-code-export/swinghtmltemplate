package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import ru.swing.html.configuration.Configuration;
import ru.swing.html.configuration.DefaultConfiguration;
import ru.swing.html.tags.Tag;

import java.io.IOException;
import java.io.InputStream;

public class DomLoader {

    private static Log logger = LogFactory.getLog(DomLoader.class);

    /**
     * Loads dom model for the html document with default configuration.
     * @param in input stream from which the html document will be readen
     * @return dom-model for the document
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel loadModel(InputStream in) throws JDOMException, IOException {
        return loadModel(in, new DefaultConfiguration());
    }

    /**
     * Loads dom model for the html document.
     * @param in input stream from which the html document will be readen
     * @param configuration model's configuration
     * @return dom-model for the document
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel loadModel(InputStream in, Configuration configuration) throws JDOMException, IOException {

        DomModel model = new DomModel();
        model.setConfiguration(configuration);
        //load libraries
        configuration.getLibraryLoader().loadLibraries();
        LibraryRegistry libraryRegistry = model.getConfiguration().getLibraryLoader().getLibraryRegistry();
        libraryRegistry.registerLibrary("", new HtmlTagFactory());

        //notify libraries
        libraryRegistry.libraryLoaded(model);

        SAXBuilder builder = new SAXBuilder();
        //disable validation and loading of external dtd
        //http://www.jdom.org/docs/faq.html#a0350
        builder.setValidation(false);
        builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        Document doc = builder.build(in);

        Element root = doc.getRootElement();
        Tag rootTag = createTag(libraryRegistry, root.getNamespacePrefix(),
                root.getNamespaceURI(), root.getName());
        rootTag.setJdomElement(root);
        rootTag.setNamespace(root.getNamespaceURI());
        rootTag.setModel(model);
        model.setRootTag(rootTag);
        parseElement(libraryRegistry, root, rootTag);
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
     * @param registry library registry
     * @param element jdom-element
     * @param tag tag
     */
    private static void parseElement(LibraryRegistry registry, Element element, Tag tag) {
        //assign tag's name
        tag.setName(element.getName().toLowerCase());
        //assign attributes
        for (java.lang.Object o : element.getAttributes()) {
            org.jdom.Attribute a = (org.jdom.Attribute) o;
            tag.setAttribute(a.getName().toLowerCase(), a.getValue());
        }
        //set default id
        if (StringUtils.isEmpty(tag.getId())) {
            tag.setId(tag.getName()+tag.getModel().nextTagCount());
        }


        //assign tag's content
        tag.setAttribute(Tag.TAG_CONTENT, element.getText());

        //recursively convert children
        for (java.lang.Object o : element.getContent()) {
//        for (java.lang.Object o : element.getChildren()) {
            if (o instanceof Element) {
                Element child = (Element) o;
                Tag childTag = createTag(registry, child.getNamespace().getPrefix(), child.getNamespaceURI(), child.getName());
                childTag.setJdomElement(child);
                tag.addContentChild(childTag);
                parseElement(registry, child, childTag);
                //child tag may substitute model
                tag.setModel(childTag.getModel());
            }
            else if (o instanceof Text) {
                Text text = (Text) o;
                tag.addContentChild(text.getText());
            }
        }

        tag.afterChildElementsConverted();

    }



    public static Tag createTag(LibraryRegistry registry, String prefix, String namespace, String name) {
        Tag tag = registry.getTagFactory(namespace).createTag(name);
        if (tag!=null) {
            tag.setNamespace(namespace);
            tag.setName(name);
            tag.setPrefix(prefix);
        }
        return tag;
    }
}
