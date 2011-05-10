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
 * Helper class
 */
public class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);

    /**
     * Converts string with integers, separated with spaces, into array.
     * @param text string with integers, e.g., "10 10 12 234"
     * @return array of integers
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
     * Splits the line into tokens with space as separator.
     * @param text line
     * @return tokens
     */
    public static String[] extractParams(String text) {
        text = mergeSpaces(text);
        return text.split(" ");
    }

    /**
     * <p>
     * Deletes double spaces in the line. All space symbols (\t etc) are replaced with space (" ").
     * </p>
     * <h2>Example:</h2?
     * "aa    aa;\tqq" -> "aa aa; qq"
     * @param text line
     * @return line without extra spaces
     */
    public static String mergeSpaces(String text) {
        text = text.replaceAll("\\s", " ");
        while (text.indexOf("  ")>=0) {
            text = text.replaceAll("  ", " ");
        }
        return text.trim();
    }

    /**
     * Returns the position of the corresponding closing bracket.
     * @param text line
     * @param openingBracketIndex the position of the opening bracket
     * @return index of the closing bracket or -1 if there is no such bracket
     */
    public static int fingMatchingClosingBracket(String text, int openingBracketIndex) {
        int outerEnd = -1;
        int currentOpened = 0;//the number of opened brackets (in case of nested brackets)
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
     * Converts the name of the class into Class type. For primitive types (int, byte etc)
     * the corresponding wrapper is used, e.g. "int" is converted to Integer.TYPE.
     *
     * The following primitive types are supported:
     * boolean, byte, short, int, char, long, float, double
     *
     * @param classname the name of class
     * @return Class
     * @throws ClassNotFoundException if there's no class with such name
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
     * Converts the string into the object of the specified class. If the object is not supported, the null is returned.
     * @param string string
     * @param type objects's type
     * @return the object with the specified type or null
     */
    public static <T> T convertStringToObject(String string, Class<T> type) {
        if (String.class.equals(type)) {
            return (T) string;
        }
        else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
            return (T) new Integer(string);
        }
        else if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
            return (T) Boolean.valueOf(string);
        }
        else if (Short.class.equals(type) || Short.TYPE.equals(type)) {
            return (T) new Short(string);
        }
        else if (Byte.class.equals(type) || Byte.TYPE.equals(type)) {
            return (T) new Byte(string);
        }
        else if (Character.class.equals(type) || Character.TYPE.equals(type)) {
            return (T) (StringUtils.isEmpty(string) ? null : string.charAt(0));
        }
        else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            return (T) new Double(string);
        }
        else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
            return (T) new Float(string);
        }
        else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
            return (T) new Long(string);
        }
        else if (Dimension.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==2) {
                return (T) new Dimension(tokens[0], tokens[1]);
            }
            else {
                logger.warn("Wrong dimension format. Correct format: 'int int'");
                return null;
            }
        }
        else if (Insets.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==4) {
                return (T) new Insets(tokens[0], tokens[1], tokens[2], tokens[3]);
            }
            else {
                logger.warn("Wrong Insets format. Correct format: 'int int int int'");
                return null;
            }
        }
        else if (Point.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==2) {
                return (T) new Point(tokens[0], tokens[1]);
            }
            else {
                logger.warn("Wrong Point format. Correct format: 'int int'");
                return null;
            }
        }
        else if (Rectangle.class.equals(type)) {
            int[] tokens = parseIntegers(string);
            if (tokens!=null && tokens.length==4) {
                return (T) new Rectangle(tokens[0], tokens[1], tokens[2], tokens[3]);
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
     * Reads InputStream into string
     * http://www.kodejava.org/examples/266.html
     * @param is inputStream
     * @return string
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
     * Searches for the method within the controller's class with the specified name. The name with no params is
     * searched first, if noone found. the name with the specified param is searched.
     * @param controllerClass controller's class within which the search will occur
     * @param name the method name
     * @param param the possible parameter type
     * @return founded method or null, of no method is found
     */
    public static Method findActionMethod(Class controllerClass, String name, Class param) {
        //search specified method
        Method method;
        try {
            //1. search no-args method
            method = controllerClass.getDeclaredMethod(name);

        } catch (NoSuchMethodException e1) {
            //2. search the method with the argument of specified type
            try {
                method = controllerClass.getDeclaredMethod(name, param);
            } catch (NoSuchMethodException e) {
                method = null;
            }
        }
        return method;
    }

}
