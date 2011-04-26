package ru.swing.html.builder;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 26.11.2010
 * Time: 12:25:36
 * </pre>
 */
public class Builder extends JFrame {

    private static Builder instance;

    public Builder() throws HeadlessException {
        instance = this;
        setTitle("SwingHtmlTemplate buider");
        setSize(1024, 768);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 3);
        setDefaultCloseOperation(EXIT_ON_CLOSE);



        MainPanel panel = new MainPanel();
//        EditorPanel panel = new EditorPanel();
        getContentPane().add(panel);

    }

    public static Builder getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new Builder().setVisible(true);
    }
}

