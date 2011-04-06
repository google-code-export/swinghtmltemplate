package ru.swing.html.tags.event;

import junit.framework.TestCase;
import ru.swing.html.Binder;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;

/**
 * Тест обработки атрибута onclick в кнопках.
 */
public class OnclickTest  extends TestCase {

    public void testOnclick() throws Exception {

        //тестируем вызов метода в контроллере без параметров
        TestClick controller = new TestClick();
        String html =
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <input id='btn' type='button' onclick='foo'/>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        assertEquals(0, controller.count);

        Binder.bind(controller, false, model);
        JButton btn = (JButton) model.getTagById("btn").getComponent();
        btn.doClick();
        assertEquals(1, controller.count);

    }

    public void testOnclick1() throws Exception {

        //тестируем вызов метода в контроллере с параметрами
        String html =
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <input id='btn' type='button' text='t1' onclick='foo'/>" +
                "   <input id='btn2' type='button' text='t2' onclick='foo'/>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        TestClick2 controller = new TestClick2();
        Binder.bind(controller, false, model);

        JButton btn = (JButton) model.getTagById("btn").getComponent();
        btn.doClick();
        assertEquals("t1", controller.text);
        JButton btn2 = (JButton) model.getTagById("btn2").getComponent();
        btn2.doClick();
        assertEquals("t2", controller.text);

    }


    public class TestClick {
        public int count = 0;

        public void foo() {
            count++;
        }

    }


    public class TestClick2 {

        public String text = "none";

        public void foo(ActionEvent e) {
            text = ((AbstractButton)e.getSource()).getText();
        }

    }

}
