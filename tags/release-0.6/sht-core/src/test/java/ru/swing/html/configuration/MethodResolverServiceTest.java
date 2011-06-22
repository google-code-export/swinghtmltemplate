package ru.swing.html.configuration;

import junit.framework.TestCase;
import ru.swing.html.*;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.io.ByteArrayInputStream;

public class MethodResolverServiceTest extends TestCase {


    public void testResolveMethod() throws Exception {



        TestTextChange controller = new TestTextChange();
        String html =
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <input id='txt' type='text' onchange='foo'/>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        assertNull(controller.text);

        Binder.bind(controller, false, model);
        JTextComponent txt = (JTextComponent) model.getTagById("txt").getComponent();
        txt.setText("test");

        assertEquals("test", controller.text);

    }

    public void testResolveCompoundMethod() throws Exception {



        TestTextChange controller = new TestTextChange();
        String html =
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <input id='txt' type='text' onchange='internal.foo1'/>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        assertNull(controller.text);

        Binder.bind(controller, false, model);
        JTextComponent txt = (JTextComponent) model.getTagById("txt").getComponent();
        txt.setText("test");

        assertEquals("foo1", controller.text);

    }

    public class TestTextChange {

        public String text = null;
        @ModelElement("internal")
        private InternalTextChange internal = new InternalTextChange();

        public void foo(DocumentEvent e) {
            try {
                text = e.getDocument().getText(0, e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        public InternalTextChange getInternal() {
            return internal;
        }

        public class InternalTextChange {

            public void foo1() {
                text = "foo1";
            }


        }


    }


}
