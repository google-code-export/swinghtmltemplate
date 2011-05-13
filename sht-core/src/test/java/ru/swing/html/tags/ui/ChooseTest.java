package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.io.ByteArrayInputStream;

public class ChooseTest extends TestCase {


    public void testTag() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body id='body'>" +
                "Start<ui:choose>" +
                "        <ui:when test=\"${type eq 'full'}\">" +
                "StartCondition<p>Full</p>EndCondition" +
                "</ui:when>" +
                "        <ui:when test=\"${type eq 'brief'}\">" +
                "StartCondition2<p>Brief</p>" +
                "EndCondition2</ui:when>" +
                "        <ui:otherwise>" +
                "StartOtherwise<p>Unknown</p>" +
                "EndOtherwise</ui:otherwise>" +
                "   </ui:choose>" +
                "End</body>\n" +
                "</html>";


        //check 1st condition (with text content)
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("type", "full");
        DomConverter.toSwing(model);

        Tag body = model.getTagById("body");
        assertEquals(5, body.getContentChildren().size());
        assertEquals("Start", body.getContentChildren().get(0));
        assertEquals("StartCondition", body.getContentChildren().get(1));
        assertEquals("p", ((Tag) body.getContentChildren().get(2)).getName());
        assertEquals("EndCondition", body.getContentChildren().get(3));
        assertEquals("End", body.getContentChildren().get(4));


        JPanel root = (JPanel) body.getComponent();
        assertEquals(1, root.getComponentCount());
        JLabel label = (JLabel) root.getComponent(0);
        assertEquals("Full", label.getText());





        //check 2nd condition
        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("type", "brief");
        DomConverter.toSwing(model);

        body = model.getTagById("body");
        assertEquals(5, body.getContentChildren().size());
        assertEquals("Start", body.getContentChildren().get(0));
        assertEquals("StartCondition2", body.getContentChildren().get(1));
        assertEquals("p", ((Tag)body.getContentChildren().get(2)).getName());
        assertEquals("EndCondition2", body.getContentChildren().get(3));
        assertEquals("End", body.getContentChildren().get(4));

        root = (JPanel) body.getComponent();
        assertEquals(1, root.getComponentCount());
        label = (JLabel) root.getComponent(0);
        assertEquals("Brief", label.getText());

        //check 3rd condition
        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("type", "123");
        DomConverter.toSwing(model);

        body = model.getTagById("body");
        assertEquals(5, body.getContentChildren().size());
        assertEquals("Start", body.getContentChildren().get(0));
        assertEquals("StartOtherwise", body.getContentChildren().get(1));
        assertEquals("p", ((Tag)body.getContentChildren().get(2)).getName());
        assertEquals("EndOtherwise", body.getContentChildren().get(3));
        assertEquals("End", body.getContentChildren().get(4));

        root = (JPanel) body.getComponent();
        assertEquals(1, root.getComponentCount());
        label = (JLabel) root.getComponent(0);
        assertEquals("Unknown", label.getText());


    }

}
