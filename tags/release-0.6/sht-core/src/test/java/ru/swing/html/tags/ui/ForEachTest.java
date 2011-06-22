package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import org.jdesktop.observablecollections.ObservableCollections;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForEachTest extends TestCase {

    public void testIterations() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:forEach items='${items}' var='i'>" +
                "      <p>${i}</p>" +
                "   </ui:forEach>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        model.addModelElement("items", Arrays.asList("1", "2", "3"));
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(3, root.getComponentCount());
        //this doesn't work because el means binding, so all labels will have text "3"
        assertEquals(JLabel.class, root.getComponent(0).getClass());
        assertEquals("1", ((JLabel)root.getComponent(0)).getText());
        assertEquals(JLabel.class, root.getComponent(1).getClass());
        assertEquals("2", ((JLabel)root.getComponent(1)).getText());
        assertEquals(JLabel.class, root.getComponent(2).getClass());
        assertEquals("3", ((JLabel)root.getComponent(2)).getText());

    }

    public void testIterationsWithTextContent() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <div id='wrapper'>Start<ui:forEach items='${items}' var='i'>" +
                "MiddleStart<p>${i}</p>MiddleEnd" +
                "</ui:forEach>End</div>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        model.addModelElement("items", Arrays.asList("1", "2"));
        DomConverter.toSwing(model);
        Tag wrapperTag = model.getTagById("wrapper");
        JComponent wrapper = wrapperTag.getComponent();
        assertNotNull(wrapper);

        assertEquals(8, wrapperTag.getContentChildren().size());
        assertEquals(2, wrapper.getComponentCount());


        assertEquals("Start", wrapperTag.getContentChildren().get(0));
        assertEquals("MiddleStart", wrapperTag.getContentChildren().get(1));
        assertEquals("p", ((Tag)wrapperTag.getContentChildren().get(2)).getName());
        assertEquals("MiddleEnd", wrapperTag.getContentChildren().get(3));
        assertEquals("MiddleStart", wrapperTag.getContentChildren().get(4));
        assertEquals("p", ((Tag)wrapperTag.getContentChildren().get(5)).getName());
        assertEquals("MiddleEnd", wrapperTag.getContentChildren().get(6));
        assertEquals("End", wrapperTag.getContentChildren().get(7));

        //this doesn't work because el means binding, so all labels will have text "3"
        assertEquals(JLabel.class, wrapper.getComponent(0).getClass());
        assertEquals("1", ((JLabel)wrapper.getComponent(0)).getText());
        assertEquals(JLabel.class, wrapper.getComponent(1).getClass());
        assertEquals("2", ((JLabel)wrapper.getComponent(1)).getText());
    }

    public void testIterationsBinding() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:forEach items='${items}' var='i' varStatus='s'>" +
                "      <p id='${s.index}' content='el'>${prefix} ${i.name}</p>" +
                "   </ui:forEach>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        List<Person> elements = ObservableCollections.observableList(Arrays.asList(new Person("1"), new Person("2"), new Person("3")));
        model.addModelElement("prefix", "My name is");
        model.addModelElement("items", elements);
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(3, root.getComponentCount());
        //this doesn't work because el means binding, so all labels will have text "3"
        assertEquals(JLabel.class, root.getComponent(0).getClass());
        assertEquals("My name is 1", ((JLabel)root.getComponent(0)).getText());
        assertEquals(JLabel.class, root.getComponent(1).getClass());
        assertEquals("My name is 2", ((JLabel)root.getComponent(1)).getText());
        assertEquals(JLabel.class, root.getComponent(2).getClass());
        assertEquals("My name is 3", ((JLabel)root.getComponent(2)).getText());

        elements.get(0).setName("4");
        assertEquals("My name is 4", ((JLabel) root.getComponent(0)).getText());

        model.addModelElement("prefix", "Hello");
        assertEquals("Hello 2", ((JLabel) root.getComponent(1)).getText());
        assertEquals("Hello 3", ((JLabel) root.getComponent(2)).getText());
        assertEquals("Hello 4", ((JLabel) root.getComponent(0)).getText());



    }


    public void testNested() throws Exception {

        String html =
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                "      xmlns:j=\"http://www.oracle.com/swing\"\n" +
                "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body style=\"display:border\">\n" +
                "\n" +
                "    <j:scroll>\n" +
                "        <j:formTable id='table'>\n" +
                "            <ui:forEach items=\"${datas}\" var=\"row\">\n" +
                "                <tr>\n" +
                "                    <ui:forEach items=\"${row}\" var=\"cell\">\n" +
                "                        <td value=\"${cell.name}\" width=\"15\"/>\n" +
                "                    </ui:forEach>\n" +
                "                </tr>\n" +
                "            </ui:forEach>\n" +
                "        </j:formTable>\n" +
                "    </j:scroll>\n" +
                "\n" +
                "\n" +
                "    <input align=\"bottom\" type='button' onclick=\"dump\" text='dump'/>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        List<List<Person>> datas = new ArrayList<List<Person>>();
        model.addModelElement("datas", datas);
        datas.add(ObservableCollections.observableList(Arrays.asList(new Person("1"), new Person("2"))));
        datas.add(ObservableCollections.observableList(Arrays.asList(new Person("3"), new Person("4"))));

        DomConverter.toSwing(model);
        JTable table = (JTable) model.query("#table").get(0).getComponent();
        TableModel tableModel = table.getModel();
        assertEquals(2, tableModel.getRowCount());
        assertEquals(2, tableModel.getColumnCount());
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("2", tableModel.getValueAt(0, 1));
        assertEquals("3", tableModel.getValueAt(1, 0));
        assertEquals("4", tableModel.getValueAt(1, 1));
    }

    public class Person {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String name;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            Object old = this.name;
            this.name = name;
            pcs.firePropertyChange("name", old, name);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

}
