package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 25.11.2010
 * Time: 17:43:11
 * </pre>
 */
public class AttributeTest extends TestCase {

    public void testCorrectWork() throws Exception {

        Div tag = new Div();
        Attribute attr = new Attribute();
        attr.setAttribute(Attribute.NAME_ATTRIBUTE, "preferredSize");
        attr.setAttribute(Attribute.VALUE_ATTRIBUTE, "101 102");
        attr.setAttribute(Attribute.TYPE_ATTRIBUTE, Dimension.class.getName());
        tag.addChild(attr);

        JComponent panel = DomConverter.convertComponent(tag);
        assertEquals(JPanel.class, panel.getClass());

        assertEquals(new Dimension(101, 102), panel.getPreferredSize());

    }

    public void testCorrectWorkInHtml() throws Exception {

        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <c:attribute name='preferredSize' value='101 102' type='java.awt.Dimension'/>"+
                "   <p content='html'>center</p>" +
                "   <p align='top'>top</p>" +
                "   <p align='bottom' content='html'><![CDATA[<i>bottom</i>]]></p>" +
                "   <p align='left'>left</p>" +
                "   <p align='right'>right</p>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        assertEquals(new Dimension(101, 102), root.getPreferredSize());


    }
}
