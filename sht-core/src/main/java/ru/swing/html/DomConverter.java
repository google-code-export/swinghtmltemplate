package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Meta;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.swing.*;

import javax.swing.*;
import javax.swing.Action;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:03:51
 * </pre>
 */
public class DomConverter {

    private static Log logger = LogFactory.getLog(DomConverter.class);

    /**
     * <p>Creates an window. The window type is set with</p>
     * <pre>
     *     &lt;meta name='display-as' content='frame'/&gt;
     * </pre>
     * <p>Possible values for "display-as" are "frame" and "dialog".</p>
     *
     * <p>If originalWindow is not null, when window is not created, originalWindow will be
     * used instead.</p>
     *
     * <p>The window title, defaultCloseOperation, modality (for JDialog) are set for window.
     * The title is taken from &lt;title>Title&lt;/title> tag.</p>
     *
     * <p>defaultCloseOperation is set with
     * <pre>
     *     &lt;meta name='onclose' content='close_op'/&gt;
     * </pre>
     * Possible values for "close_op" are:
     * <ul>
     *     <li>exit - JFrame.EXIT_ON_CLOSE (only for JFrame)</li>
     *     <li>hide - JFrame.HIDE_ON_CLOSE</li>
     *     <li>dispose - JFrame.DISPOSE_ON_CLOSE</li>
     *     <li>nothing - JFrame.DO_NOTHING_ON_CLOSE</li>
     * </ul>
     * </p>
     *
     * <p>The modality of JDialog can be set with
     * <pre>
     *     &lt;meta name='modal' content='true|false'/&gt;
     * </pre>
     * </p>
     *
     * <p>The size of window can be set with
     * <pre>
     *     &lt;meta name='size' content='w h'/&gt;
     * </pre>
     * </p>
     *
     * @param model dom-model
     * @param originalWindow if not null, will be used as window.
     * @return created window
     */
    public static Window createWindow(DomModel model, Window originalWindow) {
        Window res;
        if (originalWindow==null) {
            if ("dialog".equals(model.getMetaItems().get("display-as"))) {
                res = new JDialog();

            }
            else {
                res = new JFrame();
            }
        }
        else {
            res = originalWindow;
        }


        //apply metas
        QueryResult title = model.query("html > head > title");
        if (res instanceof JDialog) {
            JDialog dlg = (JDialog) res;
            if (title.size()>0) {
                dlg.setTitle(title.get(0).getContent());
            }

            String onclose = model.getMetaItems().get("onclose");
            if (StringUtils.isNotEmpty(onclose)) {
                if ("hide".equals(onclose)) {
                    dlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                }
                else if ("dispose".equals(onclose)) {
                    dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                }
                else if ("nothing".equals(onclose)) {
                    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                }
            }

            dlg.setModal(Utils.convertStringToObject(model.getMetaItems().get("modal"), Boolean.class));


        }
        else if (res instanceof JFrame) {
            JFrame frame = (JFrame) res;
            if (title.size()>0) {
                frame.setTitle(title.get(0).getContent());
            }

            String onclose = model.getMetaItems().get("onclose");
            if (StringUtils.isNotEmpty(onclose)) {
                if ("exit".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                else if ("hide".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                }
                else if ("dispose".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
                else if ("nothing".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }

        }


        if (StringUtils.isNotEmpty(model.getMetaItems().get("size"))) {
            Dimension d = Utils.convertStringToObject(model.getMetaItems().get("size"), Dimension.class);
            res.setSize(d);
        }

        return res;
    }


    /**
     * Creates swing component for every tag in the dom model.
     *
     * <p>When &lt;meta name='display-as' content='frame'&gt; is contained inside &lt;head&gt;
     * a window will be created (or existing window will be used if set with setWindow()),
     * the root component will be added into the contentPane of the window.</p>
     *
     * <p>Possible values for 'display-as' are "frame" and "dialog".</p>
     *
     * @param model dom-model
     * @return root swing-component
     */
    public static JComponent toSwing(DomModel model) {
        return toSwing(model, Collections.<SelectorGroup, JComponent>emptyMap());
    }

    /**
     * Creates swing component for every tag in the dom model.
     *
     * <p>When &lt;meta name='display-as' content='frame'&gt; is contained inside &lt;head&gt;
     * a window will be created (or existing window will be used if set with setWindow()),
     * the root component will be added into the contentPane of the window.</p>
     *
     * <p>Possible values for 'display-as' are "frame" and "dialog".</p>
     *
     * @param model dom-model
     * @param substitutions the map of the substitutions of the components. The key - css selector, value - the component to be used.
     * @return root swing-component
     */
    public static JComponent toSwing(DomModel model, Map<SelectorGroup, JComponent> substitutions) {

        logger.trace("Converting dom model to swing components");
        Tag html = model.getRootTag();

        logger.trace("parsing-head phase");
        Tag head = html.getChildByName("head");
        parseHead(model, head);

        logger.trace("before-component-conversion phase");
        recursivellyVisitTags(html, new TagVisitor() {
            public void visit(Tag tag) {
                tag.beforeComponentsConvertion();
            }
        });

        logger.trace("component-conversion phase");
        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body, substitutions);


        //install actions from model into root component
        for (Map.Entry<String, Action> actionPair : model.getActions().entrySet()) {
            b.getActionMap().put(actionPair.getKey(), actionPair.getValue());
        }

        logger.trace("after-conversion phase");
        recursivellyVisitTags(html, new TagVisitor() {
            public void visit(Tag tag) {
                tag.afterComponentsConverted();
            }
        });

        if (model.getMetaItems().containsKey("display-as")) {
            Window w = createWindow(model, model.getWindow());
            model.setWindow(w);
            if (w instanceof JFrame) {
                ((JFrame)w).getContentPane().add(b);
            }
            else if (w instanceof JDialog) {
                ((JDialog)w).getContentPane().add(b);
            }
        }



        return b;
    }

    /**
     * Parses tag &lt;head&gt;
     * @param model dom-model
     * @param head tag
     */
    public static void parseHead(DomModel model, Tag head) {
        if (head==null) {
            return;
        }
        for (Tag headChild : head.getChildren()) {
            if ("style".equals(headChild.getName())) {
                String css = headChild.getContent();
                List<String> cssBlocks = StyleParser.extractCssBlocks(css);
                for (String cssBlock : cssBlocks) {
                    CssBlock block = StyleParser.parseCssBlock(cssBlock);
                    model.addGlobalStyle(block);
                }
            }
            else if ("link".equals(headChild.getName()) && "stylesheet".equals(headChild.getAttribute("rel"))) {
                String filename = headChild.getAttribute("href");
                if (StringUtils.isEmpty(filename)) {
                    return;
                }

                //if absolute path
                if (filename.startsWith("/")) {
                    //getClassLoader().getResourceAsStream понимает пути без ведущего /, например,
                    //"ru/swing/html/example/loginform.css"
                    filename = filename.substring(1);
                }
                //if relative path - substitute path from dom model path
                else if (StringUtils.isNotEmpty(model.getSourcePath())) {
                    String parentPath = model.getSourcePath();
                    int indexOfFilename = parentPath.lastIndexOf('/');
                    if (indexOfFilename>=0) {
                        parentPath = parentPath.substring(0, indexOfFilename);
                    }
                    if (parentPath.startsWith("/")) {
                        parentPath = parentPath.substring(1);
                    }

                    filename = parentPath + "/" + filename;
                }

                InputStream in = DomConverter.class.getClassLoader().getResourceAsStream(filename);
                if (in!=null) {
                    try {
                        String css = Utils.readStringIntoString(in);
                        List<String> cssBlocks = StyleParser.extractCssBlocks(css);
                        for (String cssBlock : cssBlocks) {
                            CssBlock block = StyleParser.parseCssBlock(cssBlock);
                            model.addGlobalStyle(block);
                        }
                    } catch (IOException e) {
                        logger.warn("Can't read css file: "+filename);
                    }
                }
                else {
                    logger.warn("css file: "+filename+" not found");
                }
            }
            else if ("meta".equals(headChild.getName())) {
                Meta meta = (Meta) headChild;
                model.getMetaItems().put(meta.getMetaName(), meta.getMetaContent());
            }
            else if ("action".equals(headChild.getName())) {
                ru.swing.html.tags.swing.Action action = (ru.swing.html.tags.swing.Action) headChild;
                AbstractAction swingAction = action.createSwingAction();
                model.getActions().put(action.getActionName(), swingAction);
            }
        }
    }


    /**
     * Converts single tag to the swing component
     * @param componentTag tag dom-model
     * @return swing-component
     */
    public static JComponent convertComponent(Tag componentTag) {
        return convertComponent(componentTag, Collections.<SelectorGroup, JComponent>emptyMap());
    }

    /**
     * Converts dom model to the tree of swing elements
     * @param componentTag tag from the dom model
     * @param substitutions substitution map. Key - selector, value - swing component. Value is used as the tag component,
     * if tag matches selector
     * @return swing-component
     */
    public static JComponent convertComponent(Tag componentTag, Map<SelectorGroup, JComponent> substitutions) {

        //save local tag attributes (they were loaded in DonLoader)
        Map<String, String> old = new HashMap<String, String>(componentTag.getAttributes());

        //if there is dom-model, let's parse css styles
        if (componentTag.getModel()!=null) {

            //apply global (from <link> and <style>) css styles to tag
            List<CssBlock> css = componentTag.getModel().getGlobalStyles();
            for (CssBlock block : css) {
                if (block.matches(componentTag)) {
                    for (String attrName : block.getStyles().keySet()) {
                        componentTag.setAttribute(attrName, block.getStyles().get(attrName));
                    }
                }
            }
        }
        //now apply old tag attributes, so local attributes take higher priority then global.
        for (String attrName : old.keySet()) {
            String value = old.get(attrName);
            componentTag.setAttribute(attrName, value);
        }

        //create component for the tag
        JComponent component = null;
        //if component is contained in substitution map, use it
        for (SelectorGroup selector : substitutions.keySet()) {
            if (selector.matches(componentTag)) {
                component = substitutions.get(selector);
                break;
            }
        }
        //otherwise use component, created by tag
        if (component==null) {
            component = componentTag.createComponent();
        }
        componentTag.setComponent(component);
        //actual apply attributes to component
        componentTag.applyAttributes(component);
        //init layout manager
        componentTag.handleLayout();
        //parse child tags
        componentTag.handleChildren(substitutions);

        return component;
    }


    public static void recursivellyVisitTags(Tag root, TagVisitor visitor) {
        visitor.visit(root);
        List<Tag> children = new ArrayList<Tag>(root.getChildren());
        List<Tag> visited = new ArrayList<Tag>();
        for (Tag child : children) {
            recursivellyVisitTags(child, visitor);
            visited.add(child);
        }
        //check no new tags created
        List<Tag> children2 = new ArrayList<Tag>(root.getChildren());
        if (!children.containsAll(children2)) {
            children2.removeAll(children);
            for (Tag child : children2) {
                recursivellyVisitTags(child, visitor);
            }
        }
    }


}
