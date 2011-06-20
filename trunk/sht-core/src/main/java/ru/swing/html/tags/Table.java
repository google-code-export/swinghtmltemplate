package ru.swing.html.tags;

import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.layout.CellSpan;
import ru.swing.html.DomConverter;
import ru.swing.html.layout.SpanMap;
import ru.swing.html.css.CssBlock;
import ru.swing.html.layout.TableLayoutSupport;

import javax.swing.*;
import java.util.*;

/**
 * Tag will convert into JPanel with TableLayout layout manager. It will position it's children in html table manner.
 */
public class Table extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private int currentCol = 0;
    private int currentRow = 0;
    private Set<TagPosition> tags = new HashSet<TagPosition>();
    private TableLayoutSupport tableLayoutSupport;
    private SpanMap spanMap = new SpanMap();
    private List<String> widths = new ArrayList<String>();
    private List<String> heights = new ArrayList<String>();

    @Override
    public JComponent createComponent() {
        JPanel c = new JPanel();
        setComponent(c);
        return c;
    }


    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        tags.clear();
        //get the positions for every child element
        positionChildren(this);

        tableLayoutSupport = new TableLayoutSupport();
        if (getAttribute(TableLayoutSupport.COLUMN_SIZES_ATTRIBUTE)==null) {
            String columnSizes = "";
            for (int i = 0; i<widths.size(); i++) {
                String w = widths.get(i);
                if (w==null) {
                    w = "preferred";
                }
                columnSizes+= w +" ";
            }
            setAttribute(TableLayoutSupport.COLUMN_SIZES_ATTRIBUTE, columnSizes);
        }
        if (getAttribute(TableLayoutSupport.ROW_SIZES_ATTRIBUTE)==null) {
            String rowSizes = "";
            for (int i = 0; i<heights.size(); i++) {
                String w = heights.get(i);
                if (w==null) {
                    w = "preferred";
                }
                rowSizes+= w +" ";
            }
            setAttribute(TableLayoutSupport.ROW_SIZES_ATTRIBUTE, rowSizes);
        }
        getComponent().setLayout(tableLayoutSupport.createLayout(this));


        //place childs
        for (TagPosition pos : tags) {
            Tag tagAtPosition = pos.tag;

            //parent tag is <td>
            Tag td = tagAtPosition.getParent();
            String horizAlign = "f";
            String verticalAlign = "f";
            if ("left".equals(td.getTextAlign())) {
                horizAlign = "l";
            }
            else if ("right".equals(td.getTextAlign())) {
                horizAlign = "r";
            }
            else if ("center".equals(td.getTextAlign())) {
                horizAlign = "c";
            }
            else if ("full".equals(td.getTextAlign())) {
                horizAlign = "f";
            }


            if ("top".equals(td.getVerticalAlign())) {
                verticalAlign = "t";
            }
            else if ("bottom".equals(td.getVerticalAlign())) {
                verticalAlign = "b";
            }
            else if ("center".equals(td.getVerticalAlign())) {
                verticalAlign = "c";
            }
            else if ("full".equals(td.getVerticalAlign())) {
                verticalAlign = "f";
            }

            CellSpan sp = spanMap.getSpanAtLocation(pos.row, pos.col);

            //create constraint for TableLayout from position
            String align = pos.col + " " + pos.row;
            if (sp!=null) {
                align+=" "+(pos.col+sp.getColumnSpan()-1)+" "+(pos.row+sp.getRowSpan()-1);
            }
            align+=" "+horizAlign+" "+verticalAlign;

            tagAtPosition.setAttribute(ALIGN_ATTRIBUTE, align);

            JComponent childComponent = DomConverter.convertComponent(tagAtPosition, substitutions);
            tableLayoutSupport.addComponent(getComponent(), tagAtPosition.getComponentWrapper(), tagAtPosition, align);
//            tableLayoutSupport.addComponent(getComponent(), childComponent, align);
            logger.trace(toString()+ ": added '"+tagAtPosition+"' to '"+align+"'");
        }

    }

    private void positionChildren(Tag tag) {
        for (Tag child : tag.getChildren()) {
            if ("tr".equals(child.getName())) {
                currentCol=0;
                positionChildren(child);
                currentRow++;
            }
            else if ("td".equals(child.getName())) {

                //as we process <td> manually, we must manually apply global styles
                Map<String, String> old = new HashMap<String, String>();

                List<CssBlock> css = child.getModel().getGlobalStyles();
                for (CssBlock block : css) {
                    if (block.matches(child)) {
                        for (String attrName : block.getStyles().keySet()) {
                            child.setAttribute(attrName, block.getStyles().get(attrName));
                        }
                    }
                }
                for (String attrName : old.keySet()) {
                    child.setAttribute(attrName, old.get(attrName));
                }


                String rowspanStr = child.getAttribute("rowspan");
                String colspanStr = child.getAttribute("colspan");
                int rowspan = StringUtils.isNotEmpty(rowspanStr) ? new Integer(rowspanStr) : 1;
                int colspan = StringUtils.isNotEmpty(colspanStr) ? new Integer(colspanStr) : 1;

                if (colspan>1 || rowspan>1) {

                    CellSpan sp = spanMap.getSpanForCell(currentRow, currentCol);
                    if (sp!=null && (sp.getColumn()!=currentCol || sp.getRow()!=currentRow)) {
                        int xOffset = sp.getColumn()+sp.getColumnSpan()-currentCol;
                        currentCol+=xOffset;
                    }

                    spanMap.addSpan(new CellSpan(currentRow, currentCol, rowspan, colspan));
                }


                //if the cell is spanned into several columns, process its width
                if (colspan<=1) {
                    if (StringUtils.isEmpty(child.getWidth())) {//use 'preferred' if no width is specified
                        if (currentCol>widths.size()-1) {
                            widths.add(currentCol, "preferred");
                        }
                    }
                    else {//if width is specified
                        if (currentCol>widths.size()-1) {//if there was no widths stored for this column
                            widths.add(currentCol, child.getWidth());//then just store the width
                        }
                        else {//else compare with already stored and use maximum
                            if ("fill".equals(child.getWidth()) || "fill".equals(widths.get(currentCol))) {
                                //if fill is used for some cell, always use it
                                widths.set(currentCol, "fill");
                            }
                            else if ("preferred".equals(child.getWidth())) {
                                //preferred - is the lowest priority, it is used by default, so do nothing here
                            }
                            else {
                                //parse cell's width
                                Double h;
                                try {
                                    //handle percents: 'x%'
                                    if (child.getWidth().length()>1 && child.getWidth().endsWith("%")) {
                                        h = new Double(child.getWidth().substring(0, child.getWidth().length()-1))/100;
                                    }
                                    else {
                                        h = new Double(child.getWidth());
                                    }
                                } catch (NumberFormatException e) {
                                    h = TableLayout.PREFERRED;
                                }
                                //parse maximum width
                                Double max;
                                try {
                                    //handle percents: 'x%'
                                    if (widths.get(currentCol).length()>1 && widths.get(currentCol).endsWith("%")) {
                                        max = new Double(widths.get(currentCol).substring(0, widths.get(currentCol).length()-1))/100;
                                    }
                                    else {
                                        max = new Double(widths.get(currentCol));
                                    }
                                } catch (NumberFormatException e) {
                                    max = TableLayout.PREFERRED;//parsing will fail when max==preferred
                                }
                                if (h>max) {
                                    //if new value is bigger then stored, store new value
                                    widths.set(currentCol, h.toString());
                                }
                            }

                        }
                    }
                }

                //if the cell is spanned into several rows, process its height
                if (rowspan<=1) {
                    if (StringUtils.isEmpty(child.getHeight())) {//use 'preferred' if no height is specified
                        if (currentRow>heights.size()-1) {
                            heights.add(currentRow, "preferred");
                        }
                    }
                    else {//if height is specified
                        if (currentRow>heights.size()-1) {//if there was no widths stored for this row
                            heights.add(currentRow, child.getHeight().trim());//then just store the height
                        }
                        else {//else compare with already stored and use maximum
                            if ("fill".equals(child.getHeight()) || "fill".equals(heights.get(currentRow))) {
                                //if fill is used for some cell, always use it
                                heights.set(currentRow, "fill");
                            }
                            else if ("preferred".equals(child.getHeight())) {
                                //preferred - is the lowest priority, it is used by default, so do nothing here
                            }
                            else {
                                //parse cell's height
                                Double h;
                                try {
                                    //handle percents: 'x%'
                                    if (child.getHeight().length()>1 && child.getHeight().endsWith("%")) {
                                        h = new Double(child.getHeight().substring(0, child.getHeight().length()-1))/100;
                                    }
                                    else {
                                        h = new Double(child.getHeight());
                                    }
                                } catch (NumberFormatException e) {
                                    h = TableLayout.PREFERRED;
                                }
                                //parse maximum height
                                Double max;
                                try {
                                    //handle percents: 'x%'
                                    if (heights.get(currentRow).length()>1 && heights.get(currentRow).endsWith("%")) {
                                        max = new Double(heights.get(currentRow).substring(0, heights.get(currentRow).length()-1))/100;
                                    }
                                    else {
                                        max = new Double(heights.get(currentRow));
                                    }
                                } catch (NumberFormatException e) {
                                    max = TableLayout.PREFERRED;//parsing will fail when max==preferred
                                }
                                if (h>max) {
                                    //if new value is bigger then stored, store new value
                                    heights.set(currentRow, h.toString());
                                }
                            }
                        }

                    }
                }

                positionChildren(child);
                currentCol++;
            }
            else {
                CellSpan sp = spanMap.getSpanForCell(currentRow, currentCol);
                if (sp!=null && (sp.getColumn()!=currentCol || sp.getRow()!=currentRow)) {
                    int xOffset = sp.getColumn()+sp.getColumnSpan()-currentCol;
                    currentCol+=xOffset;
                }
                tags.add(new TagPosition(currentCol, currentRow, child));
            }
        }
    }


    private class TagPosition {
        private int row;
        private int col;

        private Tag tag;

        private TagPosition(int col, int row, Tag tag) {
            this.col = col;
            this.row = row;
            this.tag = tag;
        }
    }
}
