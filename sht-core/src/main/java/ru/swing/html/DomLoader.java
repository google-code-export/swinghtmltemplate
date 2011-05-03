package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ext.BeanAdapterProvider;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import ru.swing.html.tags.*;
import ru.swing.html.tags.swing.Attribute;
import ru.swing.html.tags.Object;

import java.io.*;
import java.net.URL;
import java.util.*;

public class DomLoader {

    private static Log logger = LogFactory.getLog(DomLoader.class);

    private static LibraryRegistry registry = new LibraryRegistry();
    private static final Set<ClassLoader> classLoaders = new HashSet<ClassLoader>();
    private static final Set<URL> serviceURLs = new HashSet<URL>();
    static {
        registerLibrary("", new HtmlTagFactory());
        loadProvidersIfNecessary();
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
        Tag rootTag = createTag(root.getNamespaceURI(), root.getName());
        rootTag.setNamespace(root.getNamespaceURI());
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
        //set default id
        if (StringUtils.isEmpty(tag.getId())) {
            tag.setId(tag.getName()+tag.getModel().nextTagCount());
        }


        //assign tag's content
        tag.setContent(element.getText());

        //recursively convert children
        for (java.lang.Object o : element.getChildren()) {
            Element child = (Element) o;
            Tag childTag = createTag(child.getNamespaceURI(), child.getName());
            tag.addChild(childTag);
            parseElement(child, childTag);
            //child tag may substitute model
            tag.setModel(childTag.getModel());
        }

        tag.afterChildElementsConverted();

    }



    private static void loadProvidersIfNecessary() {
        ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();

        if (!classLoaders.contains(currentLoader)) {
            classLoaders.add(currentLoader);
            loadProviders(currentLoader);
        }
    }

    private static void loadProviders(ClassLoader classLoader) {
        // PENDING: this needs to be rewriten in terms of ServiceLoader
        String serviceName = "META-INF/services/" + TagFactory.class.getName();

        try {
            Enumeration<URL> urls = classLoader.getResources(serviceName);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                if (!serviceURLs.contains(url)) {
                    serviceURLs.add(url);
                    addProviders(url);
                }
            }
        } catch (IOException ex) {
        }
    }

    private static void addProviders(URL url) {

        Properties properties = new Properties();

        logger.trace("Loading tag factories from "+url.getFile());

        try {
            properties.load(url.openStream());
            for (java.lang.Object key : properties.keySet()) {
                String classname = (String) key;
                TagFactory factory;
                try {
                    factory = (TagFactory) Class.forName(classname).newInstance();
                    registerLibrary(properties.getProperty(classname), factory);
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (ClassNotFoundException e) {
                }
            }

        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }

    }

    public static Tag createTag(String namespace, String name) {
        Tag tag = registry.getTagFactory(namespace).createTag(name);
        if (tag!=null) {
            tag.setNamespace(namespace);
            tag.setName(name);
        }
        return tag;
    }
}
