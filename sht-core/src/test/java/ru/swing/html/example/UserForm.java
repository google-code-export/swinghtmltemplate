package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;

import javax.swing.*;
import java.io.IOException;

public class UserForm {

    @Bind("rootPanel")
    private JPanel rootPanel;

    public UserForm() {
        try {
            Binder.bind(this);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public static void main(String[] args) {

        UserForm form = new UserForm();

        final JFrame f = new JFrame("Test");


        f.getContentPane().add(form.getRootPanel());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setSize(400, f.getHeight());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setVisible(true);
            }
        });

    }
}
