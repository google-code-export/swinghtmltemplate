package ru.swing.html.xhtmlrenderer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.output.DOMOutputter;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.swing.*;
import org.xhtmlrenderer.util.XRLog;
import ru.swing.html.DomConverter;
import ru.swing.html.ELUtils;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.ClickDelegator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utils for xhtmlrenderer.
 */
public class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);


    /**
     * Modifies css style string so 'attrName' attribute is set to 'attrValue'. If there is no
     * such attribute, it is added.
     *
     * <h2>Example:</h2>
     *
     * <p>let attrName=='display', attrValue='block'</p>
     *
     * <pre>
     * display: border; text-align:left;
     * </pre>
     * is converted to:
     * <pre>
     * display: block; text-align:left;
     * </pre>
     * and
     * <pre>
     * text-align:left;
     * </pre>
     * is converted to:
     * <pre>
     * display: block; text-align:left;
     * </pre>
     *
     * @param originalStyle original string with css style
     * @param attrName name of the attribute to add/modify
     * @param attrValue the value of the attrubute
     * @return modified css style with added or replaced attribute
     */
    public static String replaceCssAttribute(String originalStyle, String attrName, String attrValue) {
        if (StringUtils.isEmpty(originalStyle)) {
            return attrName+": "+attrValue+";";
        }
        Map<String, String> styles = StyleParser.extractStyles(originalStyle);
        styles.put(attrName, attrValue);
        StringBuilder modifiedStyle = new StringBuilder();
        for (String key : styles.keySet()) {
            modifiedStyle.append(key).append(":").append(styles.get(key)).append(";");
        }
        return modifiedStyle.toString();
    }


    /**
     * <p>Converts tag to jdom element. If tag containes 'renderer="swing"' attribute, then
     * it is converted using DomConverter.toSwing() and it's style is updated to contain
     * <code>'display:block'</code> attribute so xhtmlrenderer will check if this element needs to be replaced.</p>
     *
     * <p>This procedure does not handles tag's content chidlren</p>
     * @param tag original tag to convert
     * @return created element
     */
    public static Element tagToElement(Tag tag) {

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
            String modifiedStyle = replaceCssAttribute(original, "display", "block");
            el.setAttribute("style", modifiedStyle);
        }

        return el;
    }

    /**
     * Converts <code>tag</code> into jdom element and appends it to <code>root</code>.
     * Recursivelly invokes itself for every <code>tag</code> child passing created element as new <code>root</code>.
     * @param root root jdom elements.
     * @param tag tag to convert
     */
    public static void convert(Element root, Tag tag) {
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

    public static void installDocument(XHTMLPanel panel, final Tag tag) {
        //replace ReplacedElementFactory so we can inject our components
        panel.getSharedContext().setReplacedElementFactory(new SHTReplacedElementFactory(tag));


        //remove default link listener from xhtmlpanel
        List<Object> listenersToRemove = new ArrayList<Object>();
        for (Object o : panel.getMouseTrackingListeners()) {
            if (o instanceof LinkListener) {
                listenersToRemove.add(o);
            }
        }
        for (Object o : listenersToRemove) {
            panel.removeMouseTrackingListener((FSMouseListener) o);
        }
        //add custom link listener, who delegates execution to the controller
        panel.addMouseTrackingListener(new LinkListener() {
            @Override
            public void linkClicked(BasicPanel panel, String uri) {
                final Object controller = tag.getModel().getController();
                if (controller!=null) {

                    //search sprcified name
                    Method method = ru.swing.html.Utils.findActionMethod(controller.getClass(), uri, ActionEvent.class);

                    //invoke method if it is found
                    if (method!=null) {
                        if (method.getParameterTypes().length==0) {
                            try {
                                method.invoke(controller);
                            } catch (IllegalAccessException e) {
                                logger.warn(tag.toString() + ": can't invoke method " + uri + " in class " + controller.getClass().getName() + ": " + e.getMessage());
                            } catch (InvocationTargetException e) {
                                logger.warn(tag.toString() + ": can't invoke method " + uri + " in class " + controller.getClass().getName() + ": " + e.getMessage());
                            }
                        }
                        else {
                            ActionEvent e = new ActionEvent(panel, 1, "click");
                            try {
                                method.invoke(controller, e);
                            } catch (IllegalAccessException e1) {
                                logger.warn(tag.toString()+ ": can't invoke method " +uri+" in class "+controller.getClass().getName()+": "+e1.getMessage());
                            } catch (InvocationTargetException e1) {
                                logger.warn(tag.toString()+ ": can't invoke method " +uri+" in class "+controller.getClass().getName()+": "+e1.getMessage());
                            }
                        }
                    }
                    else {
                        logger.warn(tag.toString()+ ": can't find method " +uri+" in class "+controller.getClass().getName());
                    }
                }
            }
        });



        //add adapter for delegation resource resolving to our model configuration service
        panel.getSharedContext().setUserAgentCallback(new ResourseLoaderAdaper(tag));


        



        //here we create new Document for XHTMLPanel. It will contain
        //<html>
        // <head>
        //   <style>
        //     #all global styles from dom model are inserted here#
        //   </style>
        // </head>
        // <body>
        //   #all tags are converted to dom elements and inserted here#
        // </body>
        //</html>
        Element html = new Element("html", "http://www.w3.org/1999/xhtml");
        final Document doc = new Document(html);

        Element head = new Element("head", "http://www.w3.org/1999/xhtml");
        Element style = new Element("style", "http://www.w3.org/1999/xhtml");
        String css = StringUtils.join(tag.getModel().getGlobalStyles().iterator(), " ");
        style.setText(css);
        style.setAttribute("type", "text/css");
        head.addContent(style);
        html.addContent(head);

        Element body = new Element("body", "http://www.w3.org/1999/xhtml");
        body.setText(tag.getContent());
        html.addContent(body);

        convert(body, tag);

        DOMOutputter outputter = new DOMOutputter();
        try {
            org.w3c.dom.Document document = outputter.output(doc);
            panel.setDocument(document);
        } catch (JDOMException e) {
            //logger.warn("Can't create document for xhtmlpanel: "+e.getMessage());
        }
    }

    private static class ResourseLoaderAdaper extends NaiveUserAgent {
        private final Tag tag;

        public ResourseLoaderAdaper(Tag tag) {
            this.tag = tag;
        }

        @Override
        protected InputStream resolveAndOpenStream(String uri) {
            InputStream inputStream = null;
                if (StringUtils.isNotEmpty(uri)) {
                try {
                    inputStream = tag.getModel().getConfiguration().getResourceLoader().loadResource(tag.getModel(), uri);
                } catch (IOException e) {
                    logger.error(tag +": can't open stream for "+uri);
                }
            }
            return inputStream;
        }

        public ImageResource getImageResource(String uri) {
            ImageResource ir = null;
            ir = (ImageResource) _imageCache.get(uri);
            //TODO: check that cached image is still valid
            if (ir == null) {
                InputStream is = resolveAndOpenStream(uri);
                if (is != null) {
                    try {
                        BufferedImage img = ImageIO.read(is);
                        if (img == null) {
                            throw new IOException("ImageIO.read() returned null");
                        }
                        ir = createImageResource(uri, img);
                        _imageCache.put(uri, ir);
                    } catch (FileNotFoundException e) {
                        XRLog.exception("Can't read image file; image at URI '" + uri + "' not found");
                    } catch (IOException e) {
                        XRLog.exception("Can't read image file; unexpected problem for URI '" + uri + "'", e);
                    }
                }
            }
            if (ir == null) {
                ir = createImageResource(uri, null);
            }
            return ir;

        }
    }
}
