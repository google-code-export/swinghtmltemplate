package ru.swing.html;

import junit.framework.TestCase;
import ru.swing.html.tags.Div;
import ru.swing.html.tags.P;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.TextArea;

import java.io.ByteArrayInputStream;

/**
 * Test DomModel
 */
public class DomModelTest extends TestCase {

    public void testMergeComponent() throws Exception {

        DomModel model = new DomModel();


        P sourceContent = new P();

        Div source = new Div();
        source.addContentChild(sourceContent);

        Div sourceWrapper = new Div();
        sourceWrapper.addContentChild(source);


        Div target = new Div();
        TextArea targetContent = new TextArea();
        target.addContentChild(targetContent);

        model.mergeTag(source, target);

        assertEquals(1, sourceWrapper.getContentChildren().size());
        assertEquals(targetContent, sourceWrapper.getContentChildren().get(0));
    }

    public void testMergeComponent1() throws Exception {


        String html =
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <p>text1<br/>text2</p>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);



        P sourceContent = (P) model.query("body > p").get(0);
        assertEquals(3, sourceContent.getContentChildren().size());
        assertEquals(1, sourceContent.getChildren().size());


        Tag br = sourceContent.getChildren().get(0);


        Div target = new Div();
        TextArea targetContent1 = new TextArea();
        target.addContentChild(targetContent1);
        target.addContentChild("foo");

        model.mergeTag(br, target);

        assertEquals(4, sourceContent.getContentChildren().size());
        assertEquals("text1", sourceContent.getContentChildren().get(0));
        assertEquals(targetContent1, sourceContent.getContentChildren().get(1));
        assertEquals("foo", sourceContent.getContentChildren().get(2));
        assertEquals("text2", sourceContent.getContentChildren().get(3));
    }
}
