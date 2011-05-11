package ru.swing.html.mydoggy;

import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.IOException;

/**
 */
public class Example extends JPanel {

    private DomModel model;

    public Example() {
        try {
            model = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {


        Example form = new Example();
        final JFrame f = new JFrame("Test");
        f.setSize(400, 200);

        f.getContentPane().add(form);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setVisible(true);
            }
        });

    }

}
