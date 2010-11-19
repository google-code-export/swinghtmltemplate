package ru.swing.html;

import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 15:21:03
 * </pre>
 */
public class Utils {

    public static int[] parseIntegers(String text) {
        if (StringUtils.isEmpty(text)) {
            return new int[0];
        }
        String[] tokens = text.split(" ");
        java.util.List<Integer> sizes = new ArrayList<Integer>();
        for (String t : tokens) {
            if (StringUtils.isNotEmpty(t)) {
                sizes.add(new Integer(t.trim()));
            }
        }
        int[] res = new int[sizes.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = sizes.get(i);
        }
        return res;
    }

}
