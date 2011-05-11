package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;

public class ChooseTest extends TestCase {


    public void testTag() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body id='body'>\n" +
                "   <ui:choose>" +
                "        <ui:when test=\"${type eq 'full'}\">" +
                "            <p>Full</p>" +
                "        </ui:when>" +
                "        <ui:when test=\"${type eq 'brief'}\">" +
                "            <p>Brief</p>" +
                "        </ui:when>" +
                "        <ui:otherwise>" +
                "            <p>Unknown</p>" +
                "        </ui:otherwise>" +
                "   </ui:choose>" +
                "</body>\n" +
                "</html>";
        //check 1st condition
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("type", "full");
        DomConverter.toSwing(model);

        JPanel root = (JPanel) model.getTagById("body").getComponent();
        assertEquals(1, root.getComponentCount());
        JLabel label = (JLabel) root.getComponent(0);
        assertEquals("Full", label.getText());

        //check 2nd condition
        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("type", "brief");
        DomConverter.toSwing(model);

        root = (JPanel) model.getTagById("body").getComponent();
        assertEquals(1, root.getComponentCount());
        label = (JLabel) root.getComponent(0);
        assertEquals("Brief", label.getText());

        //check 3rd condition
        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("type", "123");
        DomConverter.toSwing(model);

        root = (JPanel) model.getTagById("body").getComponent();
        assertEquals(1, root.getComponentCount());
        label = (JLabel) root.getComponent(0);
        assertEquals("Unknown", label.getText());


    }
}
