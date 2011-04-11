package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 17:48:58
 * </pre>
 */
public class LoginForm extends JPanel{


    @Bind("result")
    private JLabel result;

    private DomModel model;

    @ModelElement("account")
    private Account account = new Account();

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
        result.setText("Logging in user "+account.getName());
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        Object old = this.account;
        this.account = account;
        firePropertyChange("account", old, account);
    }



    public static void main(String[] args) throws JDOMException, IOException {

        LoginForm loginForm = new LoginForm();
        Account acc = new Account();
        acc.setName("John Doe");
        loginForm.setAccount(acc);

        JFrame f = new JFrame("Test");
        f.setSize(400, 200);


        f.getContentPane().add(loginForm);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

}
