package ru.swing.html;

import junit.framework.TestCase;

import javax.swing.border.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 16:07:14
 * </pre>
 */
public class BorderFactoryTest extends TestCase {


    public void testParseEmptyBorderString() throws Exception {
        //0 param
        String borderString = "empty";
        Border border = BorderFactory.parseBorderString(borderString);
        assertTrue(border instanceof EmptyBorder);

        //4 param
        border = BorderFactory.parseBorderString("empty 12 13 14 15");
        EmptyBorder emptyBorder = (EmptyBorder) border;
        assertEquals(12, emptyBorder.getBorderInsets().top);
        assertEquals(13, emptyBorder.getBorderInsets().left);
        assertEquals(14, emptyBorder.getBorderInsets().bottom);
        assertEquals(15, emptyBorder.getBorderInsets().right);
    }



    public void testParseEtchedBorderString() throws Exception {
        Border border = BorderFactory.parseBorderString("etched");
        assertTrue(border instanceof EtchedBorder);

        border = BorderFactory.parseBorderString("etched lowered");
        assertTrue(border instanceof EtchedBorder);

        EtchedBorder etchedBorder = (EtchedBorder) border;
        assertEquals(EtchedBorder.LOWERED, etchedBorder.getEtchType());

        border = BorderFactory.parseBorderString("etched red white");
        assertTrue(border instanceof EtchedBorder);
        etchedBorder = (EtchedBorder) border;
        assertEquals(Color.red, etchedBorder.getHighlightColor());
        assertEquals(Color.white, etchedBorder.getShadowColor());

        border = BorderFactory.parseBorderString("etched raised red white");
        assertTrue(border instanceof EtchedBorder);
        etchedBorder = (EtchedBorder) border;
        assertEquals(EtchedBorder.RAISED, etchedBorder.getEtchType());
        assertEquals(Color.red, etchedBorder.getHighlightColor());
        assertEquals(Color.white, etchedBorder.getShadowColor());

    }

    public void testParseLineBorderString() throws Exception {
        Border border = BorderFactory.parseBorderString("line red");
        assertTrue(border instanceof LineBorder);
        LineBorder lineBorder = (LineBorder) border;
        assertEquals(Color.red, lineBorder.getLineColor());

        border = BorderFactory.parseBorderString("line black 2");
        assertTrue(border instanceof LineBorder);
        lineBorder = (LineBorder) border;
        assertEquals(Color.black, lineBorder.getLineColor());
        assertEquals(2, lineBorder.getThickness());

    }

    public void testParseCompoundBorderString() throws Exception {
        Border border = BorderFactory.parseBorderString("compound (empty 1 2 3 4) (compound (etched) (empty 5 6 7 8))");
        assertTrue(border instanceof CompoundBorder);
        CompoundBorder compoundBorder = (CompoundBorder) border;

        assertTrue(compoundBorder.getOutsideBorder() instanceof EmptyBorder);
        EmptyBorder emptyBorder = (EmptyBorder) compoundBorder.getOutsideBorder();
        assertEquals(1, emptyBorder.getBorderInsets().top);
        assertEquals(2, emptyBorder.getBorderInsets().left);
        assertEquals(3, emptyBorder.getBorderInsets().bottom);
        assertEquals(4, emptyBorder.getBorderInsets().right);

        assertTrue(compoundBorder.getInsideBorder() instanceof CompoundBorder);
        CompoundBorder compoundBorder2 = (CompoundBorder) compoundBorder.getInsideBorder();

        assertTrue(compoundBorder2.getOutsideBorder() instanceof EtchedBorder);
        assertTrue(compoundBorder2.getInsideBorder() instanceof EmptyBorder);
        EmptyBorder emptyBorder2 = (EmptyBorder) compoundBorder2.getInsideBorder();
        assertEquals(5, emptyBorder2.getBorderInsets().top);
        assertEquals(6, emptyBorder2.getBorderInsets().left);
        assertEquals(7, emptyBorder2.getBorderInsets().bottom);
        assertEquals(8, emptyBorder2.getBorderInsets().right);



    }


    public void testParseTitledBorderString() throws Exception {


        //1 param
        String borderString = "titled \"Foo\"";
        Border border = BorderFactory.parseBorderString(borderString);
        assertTrue(border instanceof TitledBorder);

        TitledBorder titledBorder = (TitledBorder) border;
        assertEquals("Foo", titledBorder.getTitle());

        //2 params
        borderString = "titled \"Foo\" (etched)";
        border = BorderFactory.parseBorderString(borderString);
        assertTrue(border instanceof TitledBorder);

        titledBorder = (TitledBorder) border;
        assertEquals("Foo", titledBorder.getTitle());
        assertTrue(titledBorder.getBorder() instanceof EtchedBorder);

        //4 params
        borderString = "titled \"Foo\" (etched) left bottom";
        border = BorderFactory.parseBorderString(borderString);
        assertTrue(border instanceof TitledBorder);

        titledBorder = (TitledBorder) border;
        assertEquals("Foo", titledBorder.getTitle());
        assertTrue(titledBorder.getBorder() instanceof EtchedBorder);
        assertEquals(TitledBorder.LEFT, titledBorder.getTitleJustification());
        assertEquals(TitledBorder.BOTTOM, titledBorder.getTitlePosition());

        //6 params
        borderString = "titled \"Foo\" (  empty   12   12   12   12  )   left   bottom   (Symbol)   red";
        border = BorderFactory.parseBorderString(borderString);
        assertTrue(border instanceof TitledBorder);

        titledBorder = (TitledBorder) border;
        assertEquals("Foo", titledBorder.getTitle());
        assertTrue(titledBorder.getBorder() instanceof EmptyBorder);
        EmptyBorder emptyBorder = (EmptyBorder) titledBorder.getBorder();
        assertEquals(12, emptyBorder.getBorderInsets().bottom);
        assertEquals(TitledBorder.LEFT, titledBorder.getTitleJustification());
        assertEquals(Font.decode("Symbol"), titledBorder.getTitleFont());
        assertEquals(Color.red, titledBorder.getTitleColor());


        //1 param
        borderString = "titled \"Foo:\"";
        border = BorderFactory.parseBorderString(borderString);
        assertTrue(border instanceof TitledBorder);

        titledBorder = (TitledBorder) border;
        assertEquals("Foo:", titledBorder.getTitle());



    }
}
