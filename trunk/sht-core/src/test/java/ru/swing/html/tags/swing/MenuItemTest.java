package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import javax.swing.Action;
import java.io.ByteArrayInputStream;

public class MenuItemTest extends TestCase {


    public void testBind() throws Exception {

        //test that binding of menuitem with original action works correct.
        //we will disable original action and change its title. menu item must be disabled too and have updated title

        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head>" +
                "   <c:action actionname='foo' title='foo'/>" +
                "</head>" +
                "<body style='display: border;'>" +
                "   <c:popupMenu>" +
                "      <c:menuItem actionName='foo'/>" +
                "   </c:popupMenu>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        Action a = model.getActions().get("foo");

        PopupMenu menu = (PopupMenu) model.getRootTag().getChildByName("body").getChildByName("popupmenu");
        MenuItem item = (MenuItem) menu.getChildren().get(0);
        JMenuItem menuItem = (JMenuItem) item.getComponent();
        assertTrue(menuItem.isEnabled());
        assertEquals("foo", menuItem.getText());

        a.setEnabled(false);
        a.putValue(Action.NAME, "foo1");
        assertFalse(menuItem.isEnabled());
        assertEquals("foo1", menuItem.getText());


    }
}
