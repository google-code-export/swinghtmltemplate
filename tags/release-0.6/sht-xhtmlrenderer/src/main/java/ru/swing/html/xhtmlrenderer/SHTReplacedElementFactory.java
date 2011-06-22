package ru.swing.html.xhtmlrenderer;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.swing.SwingReplacedElement;
import org.xhtmlrenderer.swing.SwingReplacedElementFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 12.05.11
 * Time: 13:24
 */
public class SHTReplacedElementFactory extends SwingReplacedElementFactory {

    private Tag owner;

    public SHTReplacedElementFactory(Tag owner) {
        this.owner = owner;
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext context, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();

        if (e == null) {
            return null;
        }

        if (needsReplacing(e)) {
            Tag child = owner.getModel().getTagById(e.getAttribute("id"));

            JComponent component;
            if (child.getComponent()==null) {
                DomConverter.convertComponent(child);
                component = child.getComponentWrapper();
                component.setSize(component.getPreferredSize());
            }
            else {
                component = child.getComponentWrapper();
            }
            if (context.isInteractive()) {
                context.getCanvas().add(component);
            }
            return new SwingReplacedElement(component);
        }
        else {
            return super.createReplacedElement(context, box, uac, cssWidth, cssHeight);
        }
    }

    public boolean needsReplacing(Element e) {
        return "swing".equals(e.getAttribute("renderer")) && StringUtils.isNotEmpty(e.getAttribute("id"));
    }
}
