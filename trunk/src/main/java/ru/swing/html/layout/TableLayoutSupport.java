package ru.swing.html.layout;

import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 13:13:17
 * </pre>
 */
public class TableLayoutSupport implements LayoutManagerSupport{

    public void addComponent(JComponent parent, JComponent child, String constraint) {
        parent.add(child, constraint);
    }

    public LayoutManager createLayout(Tag tag) {

        String colsSizes = tag.getAttribute("column-sizes");
        String rowsSizes = tag.getAttribute("row-sizes");
        double[] cols = parse(colsSizes);
        double[] rows = parse(rowsSizes);

        final TableLayout tableLayout = new TableLayout(cols,rows);
        return tableLayout;
    }

    /**
     * Парсит строку размеров. Строка должна состоять из чисел и слов fill, preffered.
     * @param colsSizes строка размеров
     * @return массив размеров, подходящий для конструктора TableLayout
     */
    private double[] parse(String colsSizes) {
        String[] tokens = colsSizes.split(" ");
        java.util.List<Double> sizes = new ArrayList<Double>();
        for (String t : tokens) {
            String token = t.trim();
            if ("fill".equals(t)) {
                sizes.add(TableLayout.FILL);
            }
            else if ("preferred".equals(t)) {
                sizes.add(TableLayout.PREFERRED);
            }
            else if (StringUtils.isNotEmpty(t)) {
                sizes.add(new Double(t.trim()));
            }
        }
        double[] res = new double[sizes.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = sizes.get(i);
        }
        return res;
    }

}
