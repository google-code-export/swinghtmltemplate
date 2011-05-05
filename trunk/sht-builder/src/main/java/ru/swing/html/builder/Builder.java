package ru.swing.html.builder;

import javax.swing.*;
import java.awt.*;

/**
 * The main frame fot the builder. Creates MainPanel and adds it to the contentPane.
 */
public class Builder extends JFrame {

    private static MainPanel instance;

    public static MainPanel getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new MainPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        instance.setLocation((screenSize.width - instance.getWidth()) / 2, (screenSize.height - instance.getHeight()) / 3);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                instance.setVisible(true);
            }
        });
    }
}

