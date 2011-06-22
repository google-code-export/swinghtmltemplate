package ru.swing.html.tags.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a composition that uses a template. Multiple compositions can use the same template,
 * thus encapsulating and reusing layout. Tag disregards everything outside of the composition,
 * which lets developers embed compositions in well-formed XHTML pages that can be viewed in an XHTML viewer,
 * such as Dreamweaver or a browser, without including extraneous elements such as head and body.
 *
 * Template is handled with 'template' attribute. The format of the "template" value must be suitable
 * for getClass().getClassLoader().getResourceAsStream();
 *
 * The snippets for insertion into template must be defined with "define" tag.
 *
 * Example:
 * <pre>
 *      &lt;!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 *        "http://www.w3.org/TR/html4/loose.dtd">
 *      &lt;html xmlns:c='http://www.oracle.com/swing'
 *            xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>
 *      &lt;head>
 *          &lt;title>&lt;/title>
 *      &lt;/head>
 *      &lt;body>
 *
 *          &lt;ui:composition template='ru/swing/html/tags/ui/CompositionTemplate.html'>
 *
 *              &lt;ui:define name='content'>
 *                  &lt;c:scroll>
 *                      &lt;textarea/>
 *                  &lt;/c:scroll>
 *              &lt;/ui:define>
 *
 *              &lt;ui:define name='control'>
 *                  &lt;div>
 *                      &lt;input type="button" text="OK"/>
 *                      &lt;input type="button" text="Cancel"/>
 *                  &lt;/div>
 *              &lt;/ui:define>
 *
 *          &lt;/ui:composition>
 *
 *
 *      &lt;/body>
 *      &lt;/html>
 * </pre>
 *
 * @see Define
 */
public class Composition extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String template;

    @Override
    public void afterChildElementsConverted() {
        if (StringUtils.isEmpty(getTemplate())) {
            logger.warn(toString()+": 'template' attribute is required.");
            return;
        }



        //loading target document model
        InputStream in = null;
        try {
            in = getModel().getConfiguration().getResourceLoader().loadResource(getModel(), getTemplate());
        } catch (IOException e) {
            in = null;
        }
//        InputStream in = getClass().getClassLoader().getResourceAsStream(getTemplate());
        DomModel target = null;
        try {
            target = DomLoader.loadModel(in);
        } catch (JDOMException e) {
            logger.error(toString() + ": Can't load document: " + getTemplate(), e);
        } catch (IOException e) {
            logger.error(toString() + ": Can't load document: " + getTemplate(), e);
        }


        if (target==null) {
            return;
        }

        //we must clear all ids because source model loader will refill them
        //and we will catch "duplicate id" warning
        target.resetIds();

        //get snippets to insert
        Map<String, Define> snippets = new HashMap<String, Define>();

        for (Tag child : getChildren()) {
            if (child instanceof Define) {
                Define snippet = (Define) child;
                String name = snippet.getSnippetName();
                snippets.put(name, snippet);
            }
        }


        //find all <insert> in target model and substitute them with snippets
        for (Tag tag : target.query("insert")) {
            if (tag instanceof Insert) {
                Insert insert = (Insert) tag;
                if (snippets.containsKey(insert.getSnippetName())) {
                    target.mergeTag(insert, snippets.get(insert.getSnippetName()));
                }
                else {
                    target.mergeTag(insert, insert);
                }
            }
        }

        setModel(target);
    }




    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("template".equals(name)) {
            setTemplate(value);
        }
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
