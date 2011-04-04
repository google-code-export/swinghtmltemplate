package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;

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
public class LoginForm extends JPanel{

    @Bind("login")
    private JTextField login;

    @Bind("password")
    private JPasswordField password;

    @Bind("ok")
    private JButton okBtn;

    @Bind("cancel")
    private JButton cancelBtn;

    @Bind("result")
    private JLabel result;

    private DomModel model;

    public LoginForm() {
        try {
            model = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onCancelClick(ActionEvent e) {
        result.setText("Cancel clicked");
    }

    public void onOkClick() {
        for (JComponent c : model.select(".button")) {
            c.setEnabled(false);
        }
        result.setText("Logging in user "+login.getText());
    }


    public static void main(String[] args) throws JDOMException, IOException {

        LoginForm loginForm = new LoginForm();

        JFrame f = new JFrame("Test");
        f.setSize(400, 200);


        f.getContentPane().add(loginForm);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

}
