package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.tags.Tag;

import javax.swing.border.Border;
import java.awt.*;

/**
 * <ul>
 *
 * <li>
 * empty: Пустая рамка. Параметров или нет, или 4 целых числа (отступа). (верх, лево, низ, право)
 * <p>
 * border: empty;<br/>
 * border: empty 12 12 12 12;<br/>
 * </p>
 * </li>
 *
 * </ul>
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 15:17:44
 * </pre>
 */
public class BorderFactory {

    private static Log logger = LogFactory.getLog(BorderFactory.class);

    public static Border createBorder(Tag tag) {

        String borderString = tag.getAttribute("border");
        return parseBorderString(borderString);
    }

    public static Border parseBorderString(String borderString) {
        int spacePos = borderString.indexOf(" ");
        if (spacePos<0) {
            spacePos = borderString.length();
        }
        String borderType = borderString.substring(0, spacePos);
        String borderParams;
        if (spacePos<borderString.length()) {
            borderParams = borderString.substring(spacePos + 1);
        }
        else {
            borderParams = null;
        }
        Border border = null;

        if ("empty".equals(borderType)) {
            int[] paddings = Utils.parseIntegers(borderParams);
            if (paddings.length==0) {
                border = javax.swing.BorderFactory.createEmptyBorder();
            }
            else if (paddings.length!=4) {
                logger.warn("Wrong border format: "+borderString);
            }
            else {
                border = javax.swing.BorderFactory.createEmptyBorder(paddings[0], paddings[1], paddings[2], paddings[3]);
            }
        }
        else if ("etched".equals(borderType)) {
            //todo разобрать возможные параметры
            border = javax.swing.BorderFactory.createEtchedBorder();
        }
        else if ("compound".equals(borderType)) {

            int outerStart = borderString.indexOf("(", spacePos+1)+1;
            int outerEnd = borderString.indexOf(")", outerStart);
            int innerStart = borderString.indexOf("(", outerEnd+1)+1;
            int innerEnd = borderString.indexOf(")", innerStart);

            String inner = borderString.substring(innerStart, innerEnd);
            String outer = borderString.substring(outerStart, outerEnd);

            final Border outerBorder = BorderFactory.parseBorderString(outer);
            final Border innerBorder = BorderFactory.parseBorderString(inner);
            border = javax.swing.BorderFactory.createCompoundBorder(outerBorder, innerBorder);
            
        }
        else if ("line".equals(borderType)) {
            int thick = -1;
            Color color = null;
            if (StringUtils.isNotEmpty(borderParams)) {
                String[] tokens = borderParams.split(" ");
                if (tokens.length==1) {
                    color = Color.getColor(tokens[0]);
                    border = javax.swing.BorderFactory.createLineBorder(color);
                }
                else if (tokens.length==2) {
                    color = Color.getColor(tokens[0]);
                    thick = new Integer(tokens[1]);
                    border = javax.swing.BorderFactory.createLineBorder(color, thick);
                }
                else {
                    logger.warn("Wrong border format: "+borderString);
                }
            }
            else {
                logger.warn("Wrong border format: "+borderString);
            }
        }
        else if ("title".equals(borderType)) {
            if (StringUtils.isNotEmpty(borderParams)) {
                //todo сейчас вся строка идет как заголовок, нужно сделать разбор. формат: border: title Foo (border: empty) 1 1 1 red
                border = javax.swing.BorderFactory.createTitledBorder(borderParams);
            }
            else {
                logger.warn("Wrong border format: "+borderString);
            }
        }
        else {
            logger.warn("Unknown border type: "+borderString);
            border = null;
        }


        return border;
    }

}
