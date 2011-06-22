package ru.swing.html.layout;

import junit.framework.TestCase;
import ru.swing.html.tags.Div;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

public class CardLayoutSupportTest extends TestCase {

    public void testCreateLayout() throws Exception {

        CardLayoutSupport support = new CardLayoutSupport();
        LayoutManager layout = support.createLayout(new Tag());
        assertNotNull(layout);
        assertEquals(CardLayout.class, layout.getClass());

    }


    public void testAddComponent() throws Exception {

        Tag parent = new Div();
        CardLayoutSupport support = new CardLayoutSupport();
        CardLayout layout = (CardLayout) support.createLayout(new Tag());
        JComponent parentComponent = parent.createComponent();
        parentComponent.setLayout(layout);

        Tag child1 = new Div();
        child1.setId("panel1");
        JComponent childComponent1 = child1.createComponent();

        Tag child2 = new Div();
        child2.setId("panel2");
        JComponent childComponent2 = child2.createComponent();


        support.addComponent(parentComponent, childComponent1, child1, null);
        support.addComponent(parentComponent, childComponent2, child2, null);

        assertEquals(2, parentComponent.getComponentCount());
        assertTrue(childComponent1.isVisible());
        assertFalse(childComponent2.isVisible());

        layout.next(parentComponent);

        assertFalse(childComponent1.isVisible());
        assertTrue(childComponent2.isVisible());

        layout.show(parentComponent, "panel1");
        assertTrue(childComponent1.isVisible());
        assertFalse(childComponent2.isVisible());

        layout.show(parentComponent, "panel2");
        assertFalse(childComponent1.isVisible());
        assertTrue(childComponent2.isVisible());

    }
}



