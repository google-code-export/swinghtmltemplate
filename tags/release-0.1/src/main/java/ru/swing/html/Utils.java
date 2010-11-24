package ru.swing.html;

import info.clearthought.layout.TableLayout;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

/**
 * Вспомогательные утилиты.
 */
public class Utils {

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
}
