package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import java.io.ByteArrayInputStream;

public class PopupMenuTest extends TestCase {

    public void testRemovedFromParentComponent() throws Exception {
        //http://code.google.com/p/swinghtmltemplate/issues/detail?id=22
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

        Tag body = model.getRootTag().getChildByName("body");
        PopupMenu menu = (PopupMenu) body.getChildByName("popupmenu");

        assertEquals(0, body.getComponent().getComponentCount());
        assertNotNull(menu.getComponent());


    }
}
