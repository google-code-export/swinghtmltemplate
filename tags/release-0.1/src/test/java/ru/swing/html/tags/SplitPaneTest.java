package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomModel;

import javax.swing.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:47:40
 * </pre>
 */
public class SplitPaneTest extends TestCase {

    public void testConvertsToJSplitPane() throws Exception {

        SplitPane tag = new SplitPane();

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JSplitPane.class, jComponent.getClass());


    }


    public void testParams() {

        SplitPane tag = new SplitPane();
        tag.setAttribute(SplitPane.ORIENTATION_ATTRIBUTE, "horizontal");
        tag.setAttribute(SplitPane.DIVIDER_SIZE_ATTRIBUTE, "10");
        tag.setAttribute(SplitPane.DIVIDER_POSITION_ATTRIBUTE, "123");

        JSplitPane jComponent = (JSplitPane) DomConverter.convertComponent(tag);
        assertEquals(JSplitPane.HORIZONTAL_SPLIT, jComponent.getOrientation());
        assertEquals(10, jComponent.getDividerSize());
        assertEquals(123, jComponent.getDividerLocation());

        //2
        tag = new SplitPane();
        tag.setAttribute(SplitPane.ORIENTATION_ATTRIBUTE, "vertical");
        tag.setAttribute(SplitPane.DIVIDER_SIZE_ATTRIBUTE, "15");
        tag.setAttribute(SplitPane.DIVIDER_POSITION_ATTRIBUTE, "13");

        jComponent = (JSplitPane) DomConverter.convertComponent(tag);
        assertEquals(JSplitPane.VERTICAL_SPLIT, jComponent.getOrientation());
        assertEquals(15, jComponent.getDividerSize());
        assertEquals(13, jComponent.getDividerLocation());

    }


    public void testChildren() {

        //1
        SplitPane tag = new SplitPane();
        tag.setAttribute(SplitPane.ORIENTATION_ATTRIBUTE, "horizontal");

        Div child1 = new Div();
        child1.setAttribute(Tag.ALIGN_ATTRIBUTE, "left");
        P child2 = new P();
        child2.setAttribute(Tag.ALIGN_ATTRIBUTE, "right");
        tag.addChild(child1);
        tag.addChild(child2);

        JSplitPane jComponent = (JSplitPane) DomConverter.convertComponent(tag);
        assertEquals(JPanel.class, jComponent.getLeftComponent().getClass());
        assertEquals(JLabel.class, jComponent.getRightComponent().getClass());

        //2
        tag = new SplitPane();
        tag.setAttribute(SplitPane.ORIENTATION_ATTRIBUTE, "horizontal");

        child1 = new Div();
        child2 = new P();
        tag.addChild(child1);
        tag.addChild(child2);

        jComponent = (JSplitPane) DomConverter.convertComponent(tag);
        assertEquals(JPanel.class, jComponent.getLeftComponent().getClass());
        assertEquals(JLabel.class, jComponent.getRightComponent().getClass());

        //3
        tag = new SplitPane();
        tag.setAttribute(SplitPane.ORIENTATION_ATTRIBUTE, "horizontal");

        child1 = new Div();
        child1.setAttribute(Tag.ALIGN_ATTRIBUTE, "left");
        child2 = new P();
        child2.setAttribute(Tag.ALIGN_ATTRIBUTE, "right");
        //reverse order
        tag.addChild(child2);
        tag.addChild(child1);

        jComponent = (JSplitPane) DomConverter.convertComponent(tag);
        assertEquals(JPanel.class, jComponent.getLeftComponent().getClass());
        assertEquals(JLabel.class, jComponent.getRightComponent().getClass());


    }

}
