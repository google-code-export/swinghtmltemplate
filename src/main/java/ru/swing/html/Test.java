package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.example.LoginForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 10:39:23
 * </pre>
 */
public class Test {

    static Log logger = LogFactory.getLog(Test.class);

    public static void main(String[] args) throws JDOMException, IOException {

        LoginForm loginForm = new LoginForm();

        JFrame f = new JFrame("Test");
        f.setSize(400, 200);


        f.getContentPane().add(loginForm.getRootPanel());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

}
