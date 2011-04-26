package ru.swing.html.css;

import junit.framework.TestCase;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Div;
import ru.swing.html.tags.P;
import ru.swing.html.tags.Tag;

import java.io.ByteArrayInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 06.12.2010
 * Time: 23:30:31
 */
public class SelectorTokensChainTest extends TestCase {

    public void testMatchesDescendant() throws Exception {

        //div p
        Tag div = new Div();
        div.setName("div");
        Tag p = new P();
        p.setName("p");
        div.addChild(p);

        //div p
        Selector selector = new Selector("div p");

        assertTrue(selector.matches(p));
        assertFalse(selector.matches(div));
    }

    public void testMatchesDescendant2() throws Exception {

        //<div><ul><p/></ul></div>
        Tag div = new Div();
        div.setName("div");
        Tag ul = new Tag();
        ul.setName("ul");
        div.addChild(ul);
        Tag p = new P();
        p.setName("p");
        ul.addChild(p);

        //div p
        Selector selector = new Selector("div p");

        assertTrue(selector.matches(p));
        assertFalse(selector.matches(div));
        assertFalse(selector.matches(ul));

        //div ul
        selector = new Selector("div ul");

        assertTrue(selector.matches(ul));
        assertFalse(selector.matches(div));
        assertFalse(selector.matches(p));

    }

    public void testMatchesChild() throws Exception {

        //<div><p/></div>
        Tag div = new Div();
        div.setName("div");
        Tag p = new P();
        p.setName("p");
        div.addChild(p);

        //div > p
        Selector selector = new Selector("div > p");

        assertTrue(selector.matches(p));
        assertFalse(selector.matches(div));
    }

    public void testMatchesChild2() throws Exception {

        //<div><ul><p/></ul></div>
        Tag div = new Div();
        div.setName("div");
        Tag ul = new Tag();
        ul.setName("ul");
        div.addChild(ul);
        Tag p = new P();
        p.setName("p");
        ul.addChild(p);

        //div > p
        Selector selector = new Selector("div > p");

        assertFalse(selector.matches(p));
        assertFalse(selector.matches(ul));
        assertFalse(selector.matches(div));

        //div > ul
        selector = new Selector("div > ul");

        assertFalse(selector.matches(p));
        assertTrue(selector.matches(ul));
        assertFalse(selector.matches(div));

        //ul > p
        selector = new Selector("ul > p");

        assertTrue(selector.matches(p));
        assertFalse(selector.matches(ul));
        assertFalse(selector.matches(div));

    }

    public void testMatchesSubling() throws Exception {

        //<div><p/><li/></div>
        Tag div = new Div();
        div.setName("div");
        Tag p = new P();
        p.setName("p");
        div.addChild(p);
        Tag li = new Tag();
        li.setName("li");
        div.addChild(li);

        //p+li
        Selector selector = new Selector("p + li");

        assertTrue(selector.matches(li));
        assertFalse(selector.matches(p));
        assertFalse(selector.matches(div));
    }


    public void testComboParentSubling() throws Exception {

        String htmlDoc =
                        "<html>" +
                        "  <body>" +
                        "    <div>" +
                        "      <ul>" +
                        "        <li>" +
                        "          <p/>" +
                        "        </li>" +
                        "      </ul>" +
                        "    </div>" +
                        "    <div>" +
                        "      <span/>" +
                        "    </div>" +
                        "  </body>" +
                        "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(htmlDoc.getBytes()));

        Tag html = model.getRootTag();
        Tag body = html.getChildren().get(0);
        Tag div1 = body.getChildren().get(0);
        Tag ul = div1.getChildren().get(0);
        Tag li = ul.getChildren().get(0);
        Tag p = li.getChildren().get(0);
        Tag div2 = body.getChildren().get(1);
        Tag span = div2.getChildren().get(0);

        //html body li p
        Selector selector = new Selector("html body li p");

        assertTrue(selector.matches(p));
        assertFalse(selector.matches(span));
        assertFalse(selector.matches(div1));
        assertFalse(selector.matches(div2));

        //html body ul>li p
        selector = new Selector("html body ul>li p");

        assertTrue(selector.matches(p));
        assertFalse(selector.matches(li));
        assertFalse(selector.matches(ul));
        assertFalse(selector.matches(span));
        assertFalse(selector.matches(div1));
        assertFalse(selector.matches(div2));

        //html  div+div span
        selector = new Selector("html div+div span");

        assertTrue(selector.matches(span));
        assertFalse(selector.matches(p));
        assertFalse(selector.matches(li));
        assertFalse(selector.matches(ul));
        assertFalse(selector.matches(div1));
        assertFalse(selector.matches(div2));

    }
}
