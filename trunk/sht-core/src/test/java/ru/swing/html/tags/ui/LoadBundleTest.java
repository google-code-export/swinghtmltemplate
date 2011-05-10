package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.util.ResourceBundle;

public class LoadBundleTest extends TestCase {

    public void testLoadingResourcesForDefaultLocale() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:loadBundle var='i18n' baseName='ru.swing.html.tags.ui.messages'/>" +
                "   <p id='p'>${i18n.default_text}</p>" +
                "</body>\n" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JLabel label = (JLabel) model.query("p#p").get(0).getComponent();

        ResourceBundle rb = ResourceBundle.getBundle("ru.swing.html.tags.ui.messages");
        String text = rb.getString("default_text");

        assertEquals(text, label.getText());


    }


    public void testLoadingResourcesForCustomLocale() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:loadBundle var='i18n' baseName='ru.swing.html.tags.ui.messages' locale='en_EN'/>" +
                "   <p id='p'>${i18n.default_text}</p>" +
                "</body>\n" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JLabel label = (JLabel) model.query("p#p").get(0).getComponent();

        assertEquals("Foo EN", label.getText());

        html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:loadBundle var='i18n' baseName='ru.swing.html.tags.ui.messages' locale='ru_RU'/>" +
                "   <p id='p'>${i18n.default_text}</p>" +
                "</body>\n" +
                "</html>";

        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        label = (JLabel) model.query("p#p").get(0).getComponent();

        assertEquals("Foo RU", label.getText());


    }



    public void testLoadingResourcesForCustomLocaleMeta() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head>\n" +
                "   <meta name='locale' content='en_EN'/>\n" +
                "</head>\n" +
                "<body>\n" +
                "   <ui:loadBundle var='i18n' baseName='ru.swing.html.tags.ui.messages' />" +
                "   <p id='p'>${i18n.default_text}</p>" +
                "</body>\n" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JLabel label = (JLabel) model.query("p#p").get(0).getComponent();

        assertEquals("Foo EN", label.getText());

        html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head>\n" +
                "   <meta name='locale' content='ru_RU'/>\n" +
                "</head>\n" +
                "<body>\n" +
                "   <ui:loadBundle var='i18n' baseName='ru.swing.html.tags.ui.messages' />" +
                "   <p id='p'>${i18n.default_text}</p>" +
                "</body>\n" +
                "</html>";

        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        label = (JLabel) model.query("p#p").get(0).getComponent();

        assertEquals("Foo RU", label.getText());


    }
}
