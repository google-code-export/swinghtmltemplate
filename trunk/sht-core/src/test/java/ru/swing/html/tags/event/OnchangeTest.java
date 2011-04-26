package ru.swing.html.tags.event;

import junit.framework.TestCase;
import ru.swing.html.Binder;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.io.ByteArrayInputStream;

public class OnchangeTest extends TestCase {

    public void testOnchange() throws Exception {

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

    public class TestTextChange {

        public String text = null;

        public void foo(DocumentEvent e) {
            try {
                text = e.getDocument().getText(0, e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

    }


}
