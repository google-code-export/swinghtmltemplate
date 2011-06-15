package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import javax.swing.Action;
import java.awt.*;
import java.io.ByteArrayInputStream;

public class ActionTest extends TestCase {

    public void testActionInHead() throws Exception {


        String html = "<html xmlns:j='http://www.oracle.com/swing'>" +
                "<head>" +
                "   <j:action actionName='load' onAction='onLoad' title='Load'/>" +
                "</head>" +
                "<body>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        assertNotNull(root.getActionMap().get("load"));
        assertEquals("Load", root.getActionMap().get("load").getValue(Action.NAME));

        

    }
}
