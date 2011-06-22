package ru.swing.html.layout;

import info.clearthought.layout.TableLayout;
import junit.framework.TestCase;

public class TableLayoutSupportTest extends TestCase {

    public void testParse() throws Exception {

        String sizes = "10 fill preferred 0.1 15%";
        double[] dsizes = TableLayoutSupport.parse(sizes);
        assertEquals(5, dsizes.length);
        assertEquals(10.0, dsizes[0]);
        assertEquals(TableLayout.FILL, dsizes[1]);
        assertEquals(TableLayout.PREFERRED, dsizes[2]);
        assertEquals(0.1, dsizes[3]);
        assertEquals(0.15, dsizes[4]);

    }
}
