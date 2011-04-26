package ru.swing.html;

import junit.framework.TestCase;
import ru.swing.html.tags.Div;
import ru.swing.html.tags.P;
import ru.swing.html.tags.TextArea;

/**
 * Test DomModel
 */
public class DomModelTest extends TestCase {

    public void testMergeComponent() throws Exception {

        DomModel model = new DomModel();


        P sourceContent = new P();

        Div source = new Div();
        source.addChild(sourceContent);

        Div sourceWrapper = new Div();
        sourceWrapper.addChild(source);


        Div target = new Div();
        TextArea targetContent = new TextArea();
        target.addChild(targetContent);

        model.mergeTag(source, target);

        assertEquals(1, sourceWrapper.getChildren().size());
        assertEquals(targetContent, sourceWrapper.getChildren().get(0));
    }
}
