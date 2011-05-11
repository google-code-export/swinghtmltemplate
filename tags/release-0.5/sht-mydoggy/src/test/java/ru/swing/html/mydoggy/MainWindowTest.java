package ru.swing.html.mydoggy;

import junit.framework.TestCase;
import org.jdom.JDOMException;
import org.noos.xing.mydoggy.*;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyDesktopContentManagerUI;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyMultiSplitContentManagerUI;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyTabbedContentManagerUI;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MainWindowTest extends TestCase {

    public void testCreateComponent() throws Exception {

        MainWindow mainWindow = new MainWindow();
        JComponent component = mainWindow.createComponent();
        assertNotNull(component);
        assertEquals(MyDoggyToolWindowManager.class, component.getClass());

    }

    public void testTypeAttribute() throws Exception {

        MainWindow mainWindow = new MainWindow();
        MyDoggyToolWindowManager component = (MyDoggyToolWindowManager) mainWindow.createComponent();

        mainWindow.setAttribute("type", "split");
        mainWindow.applyAttributes(component);
        assertEquals(MyDoggyMultiSplitContentManagerUI.class, component.getContentManager().getContentManagerUI().getClass());

        mainWindow.setAttribute("type", "tabbed");
        mainWindow.applyAttributes(component);
        assertEquals(MyDoggyTabbedContentManagerUI.class, component.getContentManager().getContentManagerUI().getClass());

        mainWindow.setAttribute("type", "desktop");
        mainWindow.applyAttributes(component);
        assertEquals(MyDoggyDesktopContentManagerUI.class, component.getContentManager().getContentManagerUI().getClass());

    }

    public void testMarkup() throws JDOMException, IOException {

        String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                "      xmlns:mydoggy=\"http://mydoggy.sourceforge.net\">\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body style=\"display: border;\">\n" +
                "\n" +
                "    <mydoggy:mainWindow align='center' type=\"desktop\">\n" +
                "        <mydoggy:toolWindow align='left' title=\"Test\" id=\"test\">\n" +
                "            <p>Hello world1!</p>\n" +
                "        </mydoggy:toolWindow>\n" +
                "\n" +
                "        <mydoggy:toolWindow align='top' title=\"Test 2\" id=\"test2\">\n" +
                "            <p>Hello world2!</p>\n" +
                "        </mydoggy:toolWindow>\n" +
                "\n" +
                "        <mydoggy:contentWindow title=\"Test 3\" id=\"testContent\">\n" +
                "            <p>Hello world3!</p>\n" +
                "        </mydoggy:contentWindow>\n" +
                "\n" +
                "        <mydoggy:contentWindow title=\"Test 4\" id=\"testContent2\">\n" +
                "            <p>Hello world4!</p>\n" +
                "        </mydoggy:contentWindow>\n" +
                "\n" +
                "    </mydoggy:mainWindow>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        JComponent root = DomConverter.toSwing(model);

        assertEquals(1, root.getComponentCount());
        assertEquals(MyDoggyToolWindowManager.class, root.getComponent(0).getClass());

        MyDoggyToolWindowManager manager = (MyDoggyToolWindowManager) root.getComponent(0);

        //test tool windows
        assertNotNull(manager.getToolWindows());
        assertEquals(2, manager.getToolWindows().length);

        org.noos.xing.mydoggy.ToolWindow tw1 = manager.getToolWindow("test");
        assertEquals("Test", tw1.getTitle());
        assertEquals("test", tw1.getId());

        assertEquals(JLabel.class, tw1.getComponent().getClass());
        JLabel l1 = (JLabel) tw1.getComponent();
        assertEquals("Hello world1!", l1.getText());


        org.noos.xing.mydoggy.ToolWindow tw2 = manager.getToolWindow("test2");
        assertEquals("Test 2", tw2.getTitle());
        assertEquals("test2", tw2.getId());

        assertEquals(JLabel.class, tw2.getComponent().getClass());
        JLabel l2 = (JLabel) tw2.getComponent();
        assertEquals("Hello world2!", l2.getText());

        //test content windows
        assertEquals(2, manager.getContentManager().getContentCount());

        Content c1 = manager.getContentManager().getContent(0);
        assertEquals("Test 3", c1.getTitle());
        assertEquals("testContent", c1.getId());
        assertEquals(JLabel.class, c1.getComponent().getClass());
        JLabel l3 = (JLabel) c1.getComponent();
        assertEquals("Hello world3!", l3.getText());

        Content c2 = manager.getContentManager().getContent(1);
        assertEquals("Test 4", c2.getTitle());
        assertEquals("testContent2", c2.getId());
        assertEquals(JLabel.class, c2.getComponent().getClass());
        JLabel l4 = (JLabel) c2.getComponent();
        assertEquals("Hello world4!", l4.getText());

    }

}
