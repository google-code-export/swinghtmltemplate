package ru.swing.html;

import junit.framework.TestCase;
import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 03.12.2010
 * Time: 14:30:25
 * </pre>
 */
public class BinderTest extends TestCase {

    public void testBind() throws Exception {

        JPanel form = new JPanel();
        String html = "<html>\n" +
                "<head></head>\n" +
                "<body style='display: border;'>\n" +
                "   <p content='html'>center</p>\n" +
                "   <p align='top'>top</p>\n" +
                "   <p align='bottom' content='html'><![CDATA[<i>bottom</i>]]></p>\n" +
                "   <p align='left'>left</p>\n" +
                "   <p align='right'>right</p>\n" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        Binder.bind(form, true, model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        assertEquals("Controller component is not used as root component", form ,root);
        assertEquals(BorderLayout.class, form.getLayout().getClass());
        assertEquals(5, form.getComponentCount());

        form = new JPanel();
        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        Binder.bind(form, false, model);
        root = model.getRootTag().getChildByName("body").getComponent();
        assertNotSame("Controller component is used as root component", form ,root);

    }

    public void testBindWithSubstitutions() throws Exception {

        JPanel form = new JPanel();

        JLabel label = new JLabel("Foo");

        String html = "<html>\n" +
                "<head></head>\n" +
                "<body style='display: border;'>\n" +
                "   <p content='html' id='rootLabel'>center</p>\n" +
                "   <p align='top'>top</p>\n" +
                "   <p align='bottom' content='html'><![CDATA[<i>bottom</i>]]></p>\n" +
                "   <p align='left'>left</p>\n" +
                "   <p align='right'>right</p>\n" +
                "</body>\n" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        Map<SelectorGroup, JComponent> substitutions = new HashMap<SelectorGroup, JComponent>();
        substitutions.put(new SelectorGroup("#rootLabel"), label);

        Binder.bind(form, true, model, substitutions);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        assertEquals("Controller component is not used as root component", form ,root);
        assertEquals(BorderLayout.class, form.getLayout().getClass());
        assertEquals(5, form.getComponentCount());

        assertEquals(label, root.getComponent(0));


    }
}
