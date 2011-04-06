package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.reflect.Method;
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
     * Преобразует название класса в объект класса. Для примитивных классов возвращается
     * соответствующий класс-обертка, например, для int вернется Integer.TYPE.
     *
     * Поддерживаются следующие простые типы:
     * boolean, byte, short, int, char, long, float, double 
     *
     * @param classname имя класса
     * @return класс
     * @throws ClassNotFoundException если класс с указанным именем не найден
     */
    public static Class convertStringToClass(String classname) throws ClassNotFoundException {
        if ("boolean".equals(classname)) {
            return Boolean.TYPE;
        }
        else if ("int".equals(classname)) {
            return Integer.TYPE;
        }
        else if ("float".equals(classname)) {
            return Float.TYPE;
        }
        else if ("double".equals(classname)) {
            return Double.TYPE;
        }
        else if ("char".equals(classname)) {
            return Character.TYPE;
        }
        else if ("long".equals(classname)) {
            return Long.TYPE;
        }
        else if ("short".equals(classname)) {
            return Short.TYPE;
        }
        else if ("byte".equals(classname)) {
            return Byte.TYPE;
        }
        else {
            return Class.forName(classname);
        }
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
        else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
            return new Integer(string);
        }
        else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
            return Boolean.valueOf(string);
        }
        else if (Short.class.equals(type) || Short.TYPE.equals(type)) {
            return new Short(string);
        }
        else if (Byte.class.equals(type) || Byte.TYPE.equals(type)) {
            return new Byte(string);
        }
        else if (Character.class.equals(type) || Character.TYPE.equals(type)) {
            return StringUtils.isEmpty(string) ? null : string.charAt(0);
        }
        else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            return new Double(string);
        }
        else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
            return new Float(string);
        }
        else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
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
        else if (Point.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==2) {
                return new Point(tokens[0], tokens[1]);
            }
            else {
                logger.warn("Wrong Point format. Correct format: 'int int'");
                return null;
            }
        }
        else if (Rectangle.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==4) {
                return new Rectangle(tokens[0], tokens[1], tokens[2], tokens[3]);
            }
            else {
                logger.warn("Wrong Rectangle format. Correct format: 'int int int int'");
                return null;
            }
        }
        else {
            logger.warn("Can't convert "+string+" to object: "+type.getName()+" is not supported");
            return null;
        }
    }


    /**
     * Вычитывает поток в строку
     * http://www.kodejava.org/examples/266.html
     * @param is поток
     * @return  строка
     * @throws IOException
     */
    public static String readStringIntoString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    /**
     * Ищет метод в классе контроллера с указанным именем. Сначала ищется
     * метод без параметров, если такой не найден, ищется метод с параметром указанного типа.
     * @param controllerClass класс контроллера, в котором ищется метод
     * @param name название метода
     * @param param тип параметра
     * @return найденный метод или null, если метод не найден
     */
    public static Method findActionMethod(Class controllerClass, String name, Class param) {
        //находим требуемый метод
        Method method;
        try {
            //1. ищем метод без параметров
            method = controllerClass.getDeclaredMethod(name);

        } catch (NoSuchMethodException e1) {
            //2. ищем метод, который принимает параметром объект ActionEvent
            try {
                method = controllerClass.getDeclaredMethod(name, param);
            } catch (NoSuchMethodException e) {
                method = null;
            }
        }
        return method;
    }

}
