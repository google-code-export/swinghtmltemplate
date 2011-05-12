package ru.swing.html.mydoggy;

import org.jdom.JDOMException;
import ru.swing.html.Binder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LayoutExample extends JFrame {

    public LayoutExample() throws HeadlessException, JDOMException, IOException {
        Binder.bind(this, true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new LayoutExample().setVisible(true);
                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
