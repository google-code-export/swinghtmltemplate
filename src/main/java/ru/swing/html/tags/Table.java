package ru.swing.html.tags;

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
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 13:27:04
 * </pre>
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
        //вычисляем позиции всех дочерних элементов
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

            tagAtPosition.setAttribute(ALIGN_ATTRIBUTE, align);

            JComponent childComponent = DomConverter.convertComponent(tagAtPosition, substitutions);
            tableLayoutSupport.addComponent(getComponent(), childComponent, align);
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

                //так как мы обрабатываем тег td вручную, то необходимо вручную применить все глобальные стили
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


                //если ячейка не тянется на несколько колонок, то обрабатываем ее ширину
                if (colspan<=1) {
                    if (StringUtils.isEmpty(child.getWidth())) {//если не указана ширина ячейки, выставляем preferred
                        if (currentCol>widths.size()-1) {
                            widths.add(currentCol, "preferred");
                        }
                    }
                    else {//если указана ширина ячейки
                        if (currentCol>widths.size()-1) {//если для данного ряда еще не было указаний по ширине
                            widths.add(currentCol, child.getWidth());//то просто сохраняем значение ширины
                        }
                        else {//иначе надо сравнить с уже сохраненным, и заменить только если указана большая высота
                            if ("fill".equals(child.getWidth()) || "fill".equals(widths.get(currentCol))) {
                                //если хоть для какой-то ячейки указан fill, то принудительно выставляем его
                                heights.set(currentCol, "fill");
                            }
                            else if ("preferred".equals(child.getWidth())) {
                                //preferred - самый низкий приоритет, он и так выставляется, ничего не делаем
                            }
                            else {
                                //парсим высоту ячейки
                                Double h;
                                try {
                                    h = new Double(child.getWidth());
                                } catch (NumberFormatException e) {
                                    h = 0d;
                                }
                                //парсим максимальную высоту
                                Double max;
                                try {
                                    max = new Double(widths.get(currentCol));
                                } catch (NumberFormatException e) {
                                    max = 0d;//не получится парсить когда max==preferred
                                }
                                if (h>max) {
                                    //если для ячейки указана больная высота, чем было сохранено ранее, то
                                    //сохраняем высоту ячейки
                                    widths.set(currentCol, h.toString());
                                }
                            }

                        }
                    }
                }

                //если ячейка не тянется на несколько рядов, то обрабатываем ее высоту
                if (rowspan<=1) {
                    if (StringUtils.isEmpty(child.getHeight())) {//если не указана высота ячейки, выставляем preferred
                        if (currentRow>heights.size()-1) {
                            heights.add(currentRow, "preferred");
                        }
                    }
                    else {//если указана высота ячейки
                        if (currentRow>heights.size()-1) {//если для данного ряда еще не было указаний по высоте
                            heights.add(currentRow, child.getHeight().trim());//то просто сохраняем значение высоты
                        }
                        else {//иначе надо сравнить с уже сохраненным, и заменить только если указана большая высота
                            if ("fill".equals(child.getHeight()) || "fill".equals(heights.get(currentRow))) {
                                //если хоть для какой-то ячейки указан fill, то принудительно выставляем его
                                heights.set(currentRow, "fill");
                            }
                            else if ("preferred".equals(child.getHeight())) {
                                //preferred - самый низкий приоритет, он и так выставляется, ничего не делаем
                            }
                            else {
                                //парсим высоту ячейки
                                Double h;
                                try {
                                    h = new Double(child.getHeight());
                                } catch (NumberFormatException e) {
                                    h = 0d;
                                }
                                //парсим максимальную высоту
                                Double max;
                                try {
                                    max = new Double(heights.get(currentRow));
                                } catch (NumberFormatException e) {
                                    max = 0d;//не получится парсить когда max==preferred
                                }
                                if (h>max) {
                                    //если для ячейки указана больная высота, чем было сохранено ранее, то
                                    //сохраняем высоту ячейки
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
