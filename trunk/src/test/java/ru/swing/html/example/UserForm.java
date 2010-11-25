package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 21.11.2010
 * Time: 0:10:55
 * To change this template use File | Settings | File Templates.
 */
public class UserForm {

    @Bind("rootPanel")
    private JPanel rootPanel;

    public UserForm() {
        try {
            Binder.bind(this);
        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public static void main(String[] args) {

        UserForm form = new UserForm();

        JFrame f = new JFrame("Test");
        f.setSize(400, 150);


        f.getContentPane().add(form.getRootPanel());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
}
