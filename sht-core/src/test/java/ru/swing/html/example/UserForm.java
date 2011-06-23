package ru.swing.html.example;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class UserForm {

    @Bind("rootPanel")
    private JPanel rootPanel;

    private DomModel model;

    private Log logger = LogFactory.getLog(getClass());

    public UserForm() {
        try {
            model = Binder.bind(this);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }


    public void cut(ActionEvent e) {
        logger.debug("Cut action invoked");
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) e.getSource();
            c.cut();
        }
    }

    public void copy(ActionEvent e) {
        logger.debug("Copy action invoked");
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) e.getSource();
            c.copy();
        }
    }

    public void paste(ActionEvent e) {
        logger.debug("Paste action invoked");
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) e.getSource();
            c.paste();
        }
    }


    public void beforePopup(MouseEvent e) {
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) e.getSource();
            model.getActions().get("cut").setEnabled(StringUtils.isNotEmpty(c.getSelectedText()));
            model.getActions().get("copy").setEnabled(StringUtils.isNotEmpty(c.getSelectedText()));
        }
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
