package ru.swing.html.layout;

import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    
    public static final String CELLSPACING_ATTRIBUTE = "cellspacing";
    public static final String ROW_SIZES_ATTRIBUTE = "x-tablelayout-row-sizes";
    public static final String COLUMN_SIZES_ATTRIBUTE = "x-tablelayout-column-sizes";

    private static Log logger = LogFactory.getLog(TableLayoutSupport.class);

    public void addComponent(JComponent parent, JComponent child, Tag childTag, String constraint) {
        parent.add(child, constraint);
    }

    public LayoutManager createLayout(Tag tag) {

        String colsSizes = tag.getAttribute(COLUMN_SIZES_ATTRIBUTE);
        String rowsSizes = tag.getAttribute(ROW_SIZES_ATTRIBUTE);
        String cellspacing = tag.getAttribute(CELLSPACING_ATTRIBUTE);
        double[] cols = parse(colsSizes);
        double[] rows = parse(rowsSizes);
        int csp = StringUtils.isNumeric(cellspacing) ? new Integer(cellspacing) : 0;

        final TableLayout tableLayout = new TableLayout(cols,rows);
        tableLayout.setHGap(csp);
        tableLayout.setVGap(csp);
        return tableLayout;
    }

    /**
     * Parses the string of sizes (columns or rows). The string must contain 'fill', 'preferred' keywords or numbers.
     * @param colsSizes the string of sizes
     * @return the array of sizes, siutable for TableLayout
     */
    public static double[] parse(String colsSizes) {
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

                //special case for 'x%'
                if (t.length()>1 && t.endsWith("%")) {
                    Double num = null;
                    try {
                        num = new Double(t.substring(0, t.length()-1))/100; //convert percent to double
                        sizes.add(num);
                    } catch (NumberFormatException e) {
                        logger.warn("Can't parse '"+num+"' string as double. Using 'preferred' value.");
                        sizes.add(TableLayout.PREFERRED);
                    }
                }
                else {
                    Double num = null;
                    try {
                        num = new Double(t.trim());
                        sizes.add(num);
                    } catch (NumberFormatException e) {
                        logger.warn("Can't parse '"+num+"' string as double. Using 'preferred' value.");
                        sizes.add(TableLayout.PREFERRED);
                    }
                }
            }
        }
        double[] res = new double[sizes.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = sizes.get(i);
        }
        return res;
    }

}
