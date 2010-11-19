package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 17:48:58
 * </pre>
 */
public class LoginForm {

    @Bind("login")
    private JTextField login;

    @Bind("password")
    private JPasswordField password;

    @Bind("ok")
    private JButton okBtn;

    @Bind("cancel")
    private JButton cancelBtn;

    @Bind("rootPanel")
    private JPanel rootPanel;

    @Bind("result")
    private JLabel result;

    public LoginForm() {
        try {
            Binder.bind(this);
        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        init();
    }

    public void init() {
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.setText("Logging in user "+login.getText());
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.setText("Cancel clicked");
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
