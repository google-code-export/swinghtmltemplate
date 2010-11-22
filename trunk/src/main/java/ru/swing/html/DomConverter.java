package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:03:51
 * </pre>
 */
public class DomConverter {

    private static Log logger = LogFactory.getLog(DomConverter.class);

    public static JComponent toSwing(DomModel model) {

        Tag html = model.getRootTag();
        Tag head = html.getChildByName("head");
        parseHead(model, head);


        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body);
        return b;
    }

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
        }
    }

    public static JComponent convertComponent(Tag componentTag) {

        Map<String, String> old = new HashMap<String, String>();

        List<CssBlock> css = componentTag.getModel().getGlobalStyles();
        for (CssBlock block : css) {
            if (block.matches(componentTag)) {
                for (String attrName : block.getStyles().keySet()) {
                    componentTag.setAttribute(attrName, block.getStyles().get(attrName));
                }
            }
        }
        for (String attrName : old.keySet()) {
            componentTag.setAttribute(attrName, old.get(attrName));
        }
        
        JComponent component = componentTag.createComponent();
        componentTag.setComponent(component);
        componentTag.applyAttributes(component);
        if (component!=null) {
            componentTag.handleLayout();
            componentTag.handleChildren();
        }      

        return component;
    }




}
