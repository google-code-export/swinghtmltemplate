package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.CellSpan;
import ru.swing.html.DomConverter;
import ru.swing.html.SpanMap;
import ru.swing.html.TableLayoutSupport;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 13:27:04
 * </pre>
 */
public class Table extends Tag {

    private int currentCol = 0;
    private int currentRow = 0;
    private Set<TagPosition> tags = new HashSet<TagPosition>();
    private TableLayoutSupport tableLayoutSupport;
    private SpanMap spanMap = new SpanMap();

    @Override
    public void handleLayout() {
        tableLayoutSupport = new TableLayoutSupport();
        getComponent().setLayout(tableLayoutSupport.createLayout(this));
    }

    @Override
    public void handleChildren() {
        //todo нет colspan, rowspan

        tags.clear();
        //вычисляем позиции всех дочерних элементов
        positionChildren(this);


        //размещаем элементы
        for (TagPosition pos : tags) {
            Tag tagAtPosition = pos.tag;

            //родительский тэг - это td
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

            //зная позицию, создаем constraint для TableLayout
            String align = pos.col + " " + pos.row;
            if (sp!=null) {
                align+=" "+(pos.col+sp.getColumnSpan()-1)+" "+(pos.row+sp.getRowSpan()-1);
            }
            align+=" "+horizAlign+" "+verticalAlign;

            tagAtPosition.setAlign(align);

            JComponent childComponent = DomConverter.convertComponent(tagAtPosition);
            tableLayoutSupport.addComponent(getComponent(), childComponent, align);
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
                String rowspanStr = child.getAttribute("rowspan");
                String colspanStr = child.getAttribute("colspan");
                if (StringUtils.isNotEmpty(rowspanStr) || StringUtils.isNotEmpty(colspanStr)) {
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
