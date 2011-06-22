package ru.swing.html.xhtmlrenderer;

import org.jdom.*;
import org.xhtmlrenderer.simple.XHTMLPanel;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

public class XhtmlRenderer extends Tag {


    @Override
    public JComponent createComponent() {
        return new XHTMLPanel();
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        XHTMLPanel panel = (XHTMLPanel) getComponent();
        Utils.installDocument(panel, this);
    }


}
