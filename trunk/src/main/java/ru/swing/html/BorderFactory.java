package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.tags.Tag;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * <ul>
 *
 * <li>
 * empty: Пустая рамка.<br/>
 * Параметров или нет, или 4 целых числа (отступа). (верх, лево, низ, право)
 * <p>
 * empty;<br/>
 * empty 12 12 12 12;<br/>
 * </p>
 * </li>
 *
 * <li>
 *    line: Рамка в виде линии<br/>
 *    Допустимые форматы:
 *    <ul>
 *       <li>line color</li>
 *       <li>line color thick</li>
 *    </ul>
 *    где
 *    <ul>
 *       <li>color - цвет линии в формате ColorFactory.getColor(String)</li>
 *       <li>thick - толщина линии в пикселях</li>
 *    </ul>
 *    <p>
 *    Примеры:
 *    <ul>
 *       <li>line black</li>
 *       <li>line red 2</li>
 *    </ul>
 *    </p>
 * </li>
 *
 * <li>
 *    etched: рамка в виде выемки
 *    Допустимые форматы:
 *    <ul>
 *       <li>etched</li>
 *       <li>etched type</li>
 *       <li>etched highlight_color shadow_color</li>
 *       <li>etched type highlight_color shadow_color</li>
 *    </ul>
 *    где
 *    <ul>
 *       <li>type - тип рамки (lowered, raised)</li>
 *       <li>highlight_color - цвет светлой части в формате ColorFactory.getColor(String)</li>
 *       <li>shadow_color - цвет теневой части в формате ColorFactory.getColor(String)</li>
 *    </ul>
 *    <p>
 *    Примеры:
 *    <ul>
 *       <li>etched</li>
 *       <li>etched lowered</li>
 *       <li>etched white black</li>
 *       <li>etched lowered white black</li>
 *    </ul>
 *    </p>
 * </li>
 *
 * <li>
 *    compound: Составная рамка. 2 параметра: внешняя рамка и внутренняя рамка.
 *    <p>
 *    compound (outer) (inner)
 *    </p>
 *    <p>
 *    Пример:<br/>
 *    compound (empty 12 12 12 12) (compound (line black) (empty 12 12 12 12))
 *    </p>
 * </li>
 *
 * <li>
 *    titled: Рамка с заголовком.<br/>
 *    Допустимые форматы:
 *    <ul>
 *       <li>titled "caption"</li>
 *       <li>titled "caption" (inner)</li>
 *       <li>titled "caption" (inner) justification position</li>
 *       <li>titled "caption" (inner) justification position (font) color</li>
 *    </ul>
 *    где
 *    <ul>
 *       <li>caption - заголовок</li>
 *       <li>inner - формат рамки</li>
 *       <li>justification - выравнивание по горизонтали (left, right, center, leading, trailing)</li>
 *       <li>position - выравнивание по вертикали (top, above-top, below-top, bottom, above-bottom, below-bottom)</li>
 *       <li>font - шрифт заголовка в формате Font.decode(String)</li>
 *       <li>color - цвет заголовка в формате ColorFactory.getColor(String)</li>
 *    </ul>
 *    <p>
 *    Примеры:
 *    <ul>
 *       <li>titled "Foo"</li>
 *       <li>titled "Foo" (empty 12 12 12 12)</li>
 *       <li>titled "Foo" (empty 12 12 12 12) left bottom</li>
 *       <li>titled "Foo" (empty 12 12 12 12) left bottom (Arial) red</li>
 *    </ul>
 *    </p>
 * </li>
 * </ul>
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 15:17:44
 * </pre>
 */
public class BorderFactory {

    private static Log logger = LogFactory.getLog(BorderFactory.class);

    /**
     * Создает рамку для компонента тега. Строка, описывающая рамку, берется из атрибута border.
     * @param tag тег
     * @return рамка
     */
    public static Border createBorder(Tag tag) {
        String borderString = tag.getAttribute(Tag.BORDER_ATTRIBUTE);
        return parseBorderString(borderString);
    }

    /**
     * Преобразует строку, описывающую рамку, в рамку.
     * @param borderString строка, описывающая рамку
     * @return рамка
     */
    public static Border parseBorderString(String borderString) {
        borderString = Utils.mergeSpaces(borderString);
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
            if (StringUtils.isNotBlank(borderParams)) {
                String[] params = Utils.extractParams(borderParams);
                if (params.length==1) {
                    int type = EtchedBorder.LOWERED;
                    if ("raised".equals(params[0])) {
                        type = EtchedBorder.RAISED;
                    }
                    else if ("lowered".equals(params[0])) {
                        type = EtchedBorder.LOWERED;
                    }
                    else {
                        logger.warn("Unknown border type: "+borderString);
                    }
                    border = javax.swing.BorderFactory.createEtchedBorder(type);
                }
                else if (params.length==2) {
                    Color highlight = ColorFactory.getColor(params[0]);
                    Color shadow = ColorFactory.getColor(params[1]);
                    border = javax.swing.BorderFactory.createEtchedBorder(highlight, shadow);
                }
                else if (params.length==3) {
                    int type = EtchedBorder.LOWERED;
                    if ("raised".equals(params[0])) {
                        type = EtchedBorder.RAISED;
                    }
                    else if ("lowered".equals(params[0])) {
                        type = EtchedBorder.LOWERED;
                    }
                    else {
                        logger.warn("Unknown border type: "+borderString);
                    }
                    Color highlight = ColorFactory.getColor(params[1]);
                    Color shadow = ColorFactory.getColor(params[2]);
                    border = javax.swing.BorderFactory.createEtchedBorder(type, highlight, shadow);
                }
            }
            else {
                border = javax.swing.BorderFactory.createEtchedBorder();
            }
        }
        else if ("compound".equals(borderType)) {

            //формат: compound (border1) (border2)
            int outerStart = borderString.indexOf("(", spacePos+1)+1;//открывающий тег первой рамки
            int outerEnd = -1;
            outerEnd = Utils.fingMatchingClosingBracket(borderString, outerStart-1);
            int innerStart = borderString.indexOf("(", outerEnd+1)+1;
            int innerEnd = Utils.fingMatchingClosingBracket(borderString, innerStart-1);

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
                String[] tokens = Utils.extractParams(borderParams);
                if (tokens.length==1) {
                    color = ColorFactory.getColor(tokens[0]);
                    border = javax.swing.BorderFactory.createLineBorder(color);
                }
                else if (tokens.length==2) {
                    color = ColorFactory.getColor(tokens[0]);
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
        else if ("titled".equals(borderType)) {
            if (StringUtils.isNotEmpty(borderParams)) {
                
                int titleStart = borderString.indexOf('"');
                int titleEnd = borderString.indexOf('"', titleStart+1);
                String title = borderString.substring(titleStart+1, titleEnd);

                int innerStart = borderString.indexOf('(');
                if (innerStart >=0) {//если в строке указана внутренняя рамка
                    int innerEnd = Utils.fingMatchingClosingBracket(borderString, innerStart);
                    Border inner = parseBorderString(borderString.substring(innerStart+1, innerEnd));
                    if (StringUtils.isNotBlank(borderString.substring(innerEnd+1))) {
                        String[] params = Utils.extractParams(borderString.substring(innerEnd+1));
                        if (params.length==2) {//указан только titleJustification
                            int titleJustification = convertTitleJustification(params[0]);
                            int titlePosition = convertTitlePosition(params[1]);
                            border = javax.swing.BorderFactory.createTitledBorder(inner, title,titleJustification, titlePosition);
                        }
                        else {

                            int fontStart = borderString.indexOf('(', innerEnd);
                            if (fontStart>=0) {
                                int fontEnd = Utils.fingMatchingClosingBracket(borderString, fontStart);
                                String fontStr = borderString.substring(fontStart+1, fontEnd);
                                Font font = Font.decode(fontStr);

                                int titleJustification = convertTitleJustification(params[0]);
                                int titlePosition = convertTitlePosition(params[1]);

                                if (StringUtils.isBlank(borderString.substring(fontEnd+1))) {//если не указан цвет
                                    border = javax.swing.BorderFactory.createTitledBorder(inner, title,titleJustification, titlePosition, font);
                                }
                                else {
                                    Color color = ColorFactory.getColor(Utils.mergeSpaces(borderString.substring(fontEnd+1)));
                                    border = javax.swing.BorderFactory.createTitledBorder(inner, title,titleJustification, titlePosition, font, color);
                                }


                            }
                            else {
                                logger.warn("Expected font declaration in border: "+borderString);
                            }


                        }
                    }
                    else {
                        border = javax.swing.BorderFactory.createTitledBorder(inner, title);
                    }
                }
                else {
                    border = javax.swing.BorderFactory.createTitledBorder(title);
                }
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

    /**
     * Переводит строку с значением параметра justification рамки TitledBorder в параметр, подходящий для TitledBorder.
     * @param justif строка
     * @return параметр
     */
    private static int convertTitleJustification(String justif) {
        int titleJustification = TitledBorder.DEFAULT_JUSTIFICATION;
        if ("left".equals(justif)) {
            titleJustification = TitledBorder.LEFT;
        }
        else if ("right".equals(justif)) {
            titleJustification = TitledBorder.LEFT;
        }
        else if ("center".equals(justif)) {
            titleJustification = TitledBorder.CENTER;
        }
        else if ("leading".equals(justif)) {
            titleJustification = TitledBorder.LEADING;
        }
        else if ("trailing".equals(justif)) {
            titleJustification = TitledBorder.TRAILING;
        }
        else {
            logger.warn("Unknown title justification: "+justif);
        }
        return titleJustification;
    }

    /**
     * Переводит строку с значением параметра position рамки TitledBorder в параметр, подходящий для TitledBorder.
     * @param pos строка
     * @return параметр
     */
    private static int convertTitlePosition(String pos) {

        int position = TitledBorder.DEFAULT_POSITION;
        if ("top".equals(pos)) {
            position = TitledBorder.TOP;
        }
        else if ("below-top".equals(pos)) {
            position = TitledBorder.BELOW_TOP;
        }
        else if ("above-top".equals(pos)) {
            position = TitledBorder.ABOVE_TOP;
        }
        else if ("bottom".equals(pos)) {
            position = TitledBorder.BOTTOM;
        }
        else if ("below-bottom".equals(pos)) {
            position = TitledBorder.BELOW_BOTTOM;
        }
        else if ("above-bottom".equals(pos)) {
            position = TitledBorder.ABOVE_BOTTOM;
        }
        else {
            logger.warn("Unknown title position: "+pos);
        }
        return position;
    }

}
