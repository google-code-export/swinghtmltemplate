package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.Utils;
import ru.swing.html.components.CompoundTableEditor;
import ru.swing.html.components.CompoundTableRenderer;
import ru.swing.html.components.TableCellBinding;
import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.lang.*;
import java.lang.Object;
import java.util.*;

/**
 *
 */
public class FormTable extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String autoresize;
    private boolean showHeader;

    @Override
    public JComponent createComponent() {
        JTable c = new JTable();
        c.setModel(new DefaultTableModel());
        setComponent(c);
        return c;
    }


    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        JTable table = (JTable) getComponent();
        if (!isShowHeader()) {
            table.setTableHeader(null);
        }

        final Set<Pair> readonlyCells = new HashSet<Pair>();


        int columnCount = 0;
        int currentColumn = 0;
        int rowCount = 0;
        for (Tag tag : getChildren()) {
            if ("tr".equals(tag.getName())) {
                if (columnCount<currentColumn) {
                    columnCount = currentColumn;
                }
                currentColumn = 0;

                for (Tag cell : tag.getChildren()) {
                    if ("td".equals(cell.getName())) {
                        if (Boolean.valueOf(cell.getAttribute("readonly"))) {
                            Pair pair = new Pair();
                            pair.column = currentColumn;
                            pair.row = rowCount;
                            readonlyCells.add(pair);
                        }
                        currentColumn++;
                    }
                }
                rowCount++;

            }
        }

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                Pair pair = new Pair();
                pair.column = column;
                pair.row = row;
                return !readonlyCells.contains(pair);
            }
        };
        model.setRowCount(rowCount);
        model.setColumnCount(columnCount);
        table.setModel(model);

        CompoundTableEditor editor = new CompoundTableEditor();
        editor.setDefaultEditor(table.getDefaultEditor(Object.class));
        table.setDefaultEditor(Object.class, editor);

        CompoundTableRenderer renderer = new CompoundTableRenderer();
        renderer.setDefaultRenderer(table.getDefaultRenderer(Object.class));
        table.setDefaultRenderer(Object.class, renderer);

        //set column autoresize mode
        if (StringUtils.isNotEmpty(getAutoresize())) {
            int mode = table.getAutoResizeMode();
            if ("off".equals(getAutoresize())) {
                mode = JTable.AUTO_RESIZE_OFF;
            }
            else if ("all".equals(getAutoresize())) {
                mode = JTable.AUTO_RESIZE_ALL_COLUMNS;
            }
            else if ("last".equals(getAutoresize())) {
                mode = JTable.AUTO_RESIZE_LAST_COLUMN;
            }
            else if ("next".equals(getAutoresize())) {
                mode = JTable.AUTO_RESIZE_NEXT_COLUMN;
            }
            else if ("auto".equals(getAutoresize())) {
                mode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS;
            }
            logger.trace(toString()+": set autoResizeMode to "+getAutoresize());
            table.setAutoResizeMode(mode);
        }



        currentColumn = 0;
        int currentRow = 0;
        for (Tag tag : getChildren()) {
            if ("tr".equals(tag.getName())) {
                currentColumn = 0;

                int maxHeight = table.getRowHeight(currentRow);
                for (Tag cell : tag.getChildren()) {
                    if ("td".equals(cell.getName())) {
                        String value = cell.getAttribute("value");
                        if (StringUtils.isNotEmpty(value)) {
                            new TableCellBinding(AutoBinding.UpdateStrategy.READ_WRITE, getModel().getModelElements(),
                                    ELProperty.create(value), table, currentRow, currentColumn, "foo").bind();
                        }
                        else {
                            model.setValueAt(cell.getContent(), currentRow, currentColumn);
                        }

                        //set column width
                        TableColumnModel columnModel = table.getColumnModel();
                        if (StringUtils.isNotEmpty(cell.getAttribute("width"))) {
                            Integer w = (Integer) Utils.convertStringToObject(cell.getAttribute("width"), Integer.class);
                            columnModel.getColumn(currentColumn).setWidth(w);
                            columnModel.getColumn(currentColumn).setPreferredWidth(w);
                            logger.trace(toString()+": set column width="+w+" for column "+currentColumn);
                        }

                        //set row height
                        if (StringUtils.isNotEmpty(cell.getAttribute("height"))) {
                            Integer h = (Integer) Utils.convertStringToObject(cell.getAttribute("height"), Integer.class);
                            if (h>maxHeight) {
                                maxHeight = h;
                            }
                        }

                        //install editor
                        String editorAttr = cell.getAttribute("editor");
                        if (StringUtils.isNotEmpty(editorAttr)) {
                            ELProperty rendererProperty = ELProperty.create(editorAttr);
                            Object editorVal = rendererProperty.getValue(getModel().getModelElements());
                            if (editorVal instanceof TableCellEditor) {
                                editor.addEditor((TableCellEditor) editorVal, currentRow, currentColumn);
                                logger.trace(toString()+": set cell ["+currentRow+", "+currentColumn+"] editor: '"+editorAttr+"'");
                            }
                            else if (editor == null) {
                                logger.warn(toString()+ ": can't set cell ["+currentRow+", "+currentColumn+"] editor. Object '"+editorAttr + " is null");
                            }
                            else {
                                logger.warn(toString()+ ": can't set cell ["+currentRow+", "+currentColumn+"] editor. Object '"+editorAttr + " is not instance of "+TableCellEditor.class.getName());
                            }

                        }

                        //install renderer
                        String rendererAttr = cell.getAttribute("renderer");
                        if (StringUtils.isNotEmpty(rendererAttr)) {
                            ELProperty rendererProperty = ELProperty.create(rendererAttr);
                            Object rendererVal = rendererProperty.getValue(getModel().getModelElements());
                            if (rendererVal instanceof TableCellRenderer) {
                                renderer.addRenderer((TableCellRenderer) rendererVal, currentRow, currentColumn);
                                logger.trace(toString() + ": set cell [" + currentRow + ", " + currentColumn + "] renderer: '" + rendererAttr + "'");
                            }
                            else if (renderer == null) {
                                logger.warn(toString()+ ": can't set cell ["+currentRow+", "+currentColumn+"] renderer. Object '"+rendererAttr + " is null");
                            }
                            else {
                                logger.warn(toString()+ ": can't set cell ["+currentRow+", "+currentColumn+"] renderer. Object '"+rendererAttr + " is not instance of "+TableCellRenderer.class.getName());
                            }

                        }

                        currentColumn++;
                    }
                }
                //set row height
                if (maxHeight!=table.getRowHeight(currentRow)) {
                    table.setRowHeight(currentRow, maxHeight);
                    logger.trace(toString()+": set row height="+maxHeight+" for row "+currentRow);
                }

                currentRow++;
            }
        }


    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("autoresize".equals(name)) {
            setAutoresize(value);
        }
        else if ("showheader".equals(name)) {
            setShowHeader(Boolean.valueOf(value));
        }
    }



    public String getAutoresize() {
        return autoresize;
    }

    public void setAutoresize(String autoresize) {
        this.autoresize = autoresize;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    class Pair {
        public int column, row;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (column != pair.column) return false;
            if (row != pair.row) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = column;
            result = 31 * result + row;
            return result;
        }
    }
}
