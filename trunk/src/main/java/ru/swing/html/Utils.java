package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.util.ArrayList;

/**
 * Вспомогательные утилиты.
 */
public class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);

    /**
     * Преобразует строку чисел в массив чисел.
     * @param text строка чисел, например, "10 10 12 234"
     * @return массив чисел
     */
    public static int[] parseIntegers(String text) {
        if (StringUtils.isEmpty(text)) {
            return new int[0];
        }
        text = mergeSpaces(text);
        String[] tokens = text.split(" ");
        java.util.List<Integer> sizes = new ArrayList<Integer>();
        for (String t : tokens) {
            if (StringUtils.isNotEmpty(t) && StringUtils.isNumeric(t)) {
                sizes.add(new Integer(t.trim()));
            }
        }
        int[] res = new int[sizes.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = sizes.get(i);
        }
        return res;
    }

    /**
     * Разбивает строку на параметры. Разделителем выступает пробельный символ.
     * @param text строка
     * @return параметры
     */
    public static String[] extractParams(String text) {
        text = mergeSpaces(text);
        return text.split(" ");
    }

    /**
     * Удаляет лишние пробельные символы в строке. Все пробельные символы переводит в пробелы.
     * Пример:
     * "aa    aa;\tqq" -> "aa aa; qq"
     * @param text строка
     * @return строка без лишних пробелов
     */
    public static String mergeSpaces(String text) {
        text = text.replaceAll("\\s", " ");
        while (text.indexOf("  ")>=0) {
            text = text.replaceAll("  ", " ");
        }
        return text.trim();
    }

    /**
     * Возвращает индекс позиции, в которой находится соответствующая закрывающая скобка
     * @param text строка
     * @param openingBracketIndex индекс открывающей скобки
     * @return индекс закрывающей скобки или -1, если такой скобки нет
     */
    public static int fingMatchingClosingBracket(String text, int openingBracketIndex) {
        int outerEnd = -1;
        int currentOpened = 0;//количество открытых скобок (при вложенных скобках)
        for (int i = openingBracketIndex+1; i<text.length() && outerEnd<0; i++) {
            char c = text.charAt(i);
            if (c =='(') {
                currentOpened++;
            }
            else if (c ==')') {
                if (currentOpened>0) {
                    currentOpened--;
                }
                else {
                    outerEnd = i;
                }
            }
        }
        return outerEnd;
    }


    /**
     * Преобразует строку в объект указанного типа. Если объект не поддерэивается, возвращается null.
     * @param string строка
     * @param type тип объекта
     * @return объект указанного типа или null
     */
    public static java.lang.Object convertStringToObject(String string, Class type) {
        if (String.class.equals(type)) {
            return string;
        }
        else if (Integer.class.equals(type)) {
            return new Integer(string);
        }
        else if (Double.class.equals(type)) {
            return new Double(string);
        }
        else if (Float.class.equals(type)) {
            return new Float(string);
        }
        else if (Long.class.equals(type)) {
            return new Long(string);
        }
        else if (Dimension.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==2) {
                return new Dimension(tokens[0], tokens[1]);
            }
            else {
                logger.warn("Wrong dimension format. Correct format: 'int int'");
                return null;
            }
        }
        else if (Insets.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==4) {
                return new Insets(tokens[0], tokens[1], tokens[2], tokens[3]);
            }
            else {
                logger.warn("Wrong Insets format. Correct format: 'int int int int'");
                return null;
            }
        }
        else {
            logger.warn("Can't convert "+string+" to object: "+type.getName()+" is not supported");
            return null;
        }
    }

}
