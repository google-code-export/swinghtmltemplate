package ru.swing.html.configuration;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.io.ByteArrayInputStream;

public class DefaultAttributeParserTest extends TestCase {


    public void testIconParser() throws Exception {
        //http://code.google.com/p/swinghtmltemplate/issues/detail?id=20
        //Issue 20:	icon attribute in css block should support delimeters
        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head>" +
                "   <style>" +
                "      #foo {" +
                "          icon: '/img/accept.png'" +
                "      }" +
                "   </style>" +
                "</head>" +
                "<body style='display: border;'>" +
                "   <input type='button' id='foo'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        Tag b = model.getTagById("foo");
        JButton button = (JButton) b.getComponent();
        Icon icon = button.getIcon();
        assertNotNull(icon);
        assertEquals(16, icon.getIconHeight());
        assertEquals(16, icon.getIconWidth());


    }
}
