package ru.swing.html.css;

import org.apache.batik.css.parser.Parser;
import org.w3c.css.sac.SelectorList;

import java.io.IOException;

/**
 * it works!
 */
public class BatikTest {

    public static void main(String[] args) throws IOException {
        Parser p = new Parser();
        SelectorList l = p.parseSelectors("div li > p[id=qq]");
        System.out.println(l);
    }
}
