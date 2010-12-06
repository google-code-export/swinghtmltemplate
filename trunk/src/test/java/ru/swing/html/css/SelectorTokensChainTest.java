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
        SelectorTokensChain chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(p));
        assertFalse(chain.matches(div));
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
        SelectorTokensChain chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(p));
        assertFalse(chain.matches(div));
        assertFalse(chain.matches(ul));

        //div ul
        chain = new SelectorTokensChain();
        chain.append(new SelectorToken("ul"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(ul));
        assertFalse(chain.matches(div));
        assertFalse(chain.matches(p));

    }

    public void testMatchesChild() throws Exception {

        //<div><p/></div>
        Tag div = new Div();
        div.setName("div");
        Tag p = new P();
        p.setName("p");
        div.addChild(p);

        //div > p
        SelectorTokensChain chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.PARENT);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(p));
        assertFalse(chain.matches(div));
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
        SelectorTokensChain chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.PARENT);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);

        assertFalse(chain.matches(p));
        assertFalse(chain.matches(ul));
        assertFalse(chain.matches(div));

        //div > ul
        chain = new SelectorTokensChain();
        chain.append(new SelectorToken("ul"), SelectorTokenRelation.PARENT);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);

        assertFalse(chain.matches(p));
        assertTrue(chain.matches(ul));
        assertFalse(chain.matches(div));

        //ul > p
        chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.PARENT);
        chain.append(new SelectorToken("ul"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(p));
        assertFalse(chain.matches(ul));
        assertFalse(chain.matches(div));

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
        SelectorTokensChain chain = new SelectorTokensChain();
        chain.append(new SelectorToken("li"), SelectorTokenRelation.SUBLING);
        chain.append(new SelectorToken("p"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(li));
        assertFalse(chain.matches(p));
        assertFalse(chain.matches(div));
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
        SelectorTokensChain chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("li"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("body"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("html"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(p));
        assertFalse(chain.matches(span));
        assertFalse(chain.matches(div1));
        assertFalse(chain.matches(div2));

        //html body ul>li p
        chain = new SelectorTokensChain();
        chain.append(new SelectorToken("p"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("li"), SelectorTokenRelation.PARENT);
        chain.append(new SelectorToken("ul"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("body"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("html"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(p));
        assertFalse(chain.matches(li));
        assertFalse(chain.matches(ul));
        assertFalse(chain.matches(span));
        assertFalse(chain.matches(div1));
        assertFalse(chain.matches(div2));

        //html  div+div span
        chain = new SelectorTokensChain();
        chain.append(new SelectorToken("span"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.SUBLING);
        chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);
        chain.append(new SelectorToken("html"), SelectorTokenRelation.ANY);

        assertTrue(chain.matches(span));
        assertFalse(chain.matches(p));
        assertFalse(chain.matches(li));
        assertFalse(chain.matches(ul));
        assertFalse(chain.matches(div1));
        assertFalse(chain.matches(div2));





    }
}
