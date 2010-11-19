package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:03:51
 * </pre>
 */
public class DomConverter {

    private static Log logger = LogFactory.getLog(DomConverter.class);

    public static JComponent toSwing(DomModel model) {

        Tag html = model.getRootTag();
        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body);
        return b;
    }

    public static JComponent convertComponent(Tag componentTag) {

        JComponent component = createComponent(componentTag);
        componentTag.setComponent(component);
        if (component!=null) {
            componentTag.handleLayout();
            componentTag.handleChildren();
        }      

        return component;
    }


    public static JComponent createComponent(Tag tag) {

        if ("component".equals(tag.getName())) {
            String classname = tag.getType();
            JComponent c;
            Class cl;
            try {
                cl = Class.forName(classname);
                c = (JComponent) cl.newInstance();
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Can't create component for class "+classname);
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("Can't create component for class "+classname);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Can't create component for class "+classname);
            }

            tag.applyAttributes(c);

            return c;

        }
        else if ("body".equals(tag.getName())) {
            final JPanel panel = new JPanel();
            tag.applyAttributes(panel);
            if (tag.getLayout()==null) {
                tag.setLayout("flow");
            }
            return panel;
        }
        else if ("div".equals(tag.getName())) {
            final JPanel panel = new JPanel();
            tag.applyAttributes(panel);
            if (tag.getLayout()==null) {
                tag.setLayout("flow");
            }
            return panel;
        }
        else if ("input".equals(tag.getName())) {

            String type = tag.getType();

            JComponent field;
            if ("text".equals(type)) {
                field = new JTextField();
            }
            else if ("button".equals(type)) {
                field = new JButton();
            }
            else if ("password".equals(type)) {
                field = new JPasswordField();
            }
            else if ("checkbox".equals(type)) {
                field = new JCheckBox();
            }
            else if ("radio".equals(type)) {
                field = new JRadioButton();
            }
            else {
                field = new JTextField();
            }
            tag.applyAttributes(field);

            if (field instanceof JTextComponent) {
                JTextComponent c = (JTextComponent) field;
                c.setText(tag.getContent());
            }

            return field;
        }
        else if ("p".equals(tag.getName())) {
            final JLabel label = new JLabel();
            tag.applyAttributes(label);
            label.setText("<html>"+tag.getContent()+"</html>");
            if (tag.getAttribute("layout")==null) {
                tag.setAttribute("layout", "flow");
            }
            return label;
        }

        else if ("table".equals(tag.getName())) {
            final JPanel panel = new JPanel();
            tag.applyAttributes(panel);
            tag.setLayout("table");
            return panel;

        }
        else if ("scroll".equals(tag.getName())) {
            final JScrollPane panel = new JScrollPane();
            tag.applyAttributes(panel);
            return panel;
        }
        else if ("split".equals(tag.getName())) {
            final JSplitPane panel = new JSplitPane();
            tag.applyAttributes(panel);
            return panel;
        }
        else if ("img".equals(tag.getName())) {
            final JLabel l = new JLabel();
            tag.setAttribute("icon", tag.getAttribute("src"));
            tag.applyAttributes(l);
            return l;
        }
        else if ("textarea".equals(tag.getName())) {
            final JTextArea area = new JTextArea();
            tag.applyAttributes(area);
            area.setText(tag.getContent());
            return area;
        }

        logger.warn("Unknown tag "+tag.getName());
        return null;
        //throw new IllegalArgumentException("Unknown tag "+tag.getName());
    }


}
