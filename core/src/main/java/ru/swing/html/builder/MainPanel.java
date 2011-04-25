package ru.swing.html.builder;

import org.jdom.JDOMException;
import ru.swing.html.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 26.11.2010
 * Time: 12:26:36
 * </pre>
 */
public class MainPanel extends JPanel {

    @Bind("content")
    private JTextArea content;


    public MainPanel() {
        try {
            Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void clear() {
        content.setText("<html>\n" +
                "<body>\n" +
                "<p>qq</p>\n" +
                "</body>\n" +
                "</html>");
    }

    public void build() {
        String html = content.getText();
        InputStream in = new ByteArrayInputStream(html.getBytes());
        try {
            DomModel model = DomLoader.loadModel(in);

            PreviewPanel previewPanel = new PreviewPanel();
            previewPanel.setModel(model);
            previewPanel.compose();


            Builder builder = Builder.getInstance();
            JDialog preview = new JDialog(builder, "Preview");
            preview.setSize(500, 400);
            preview.setLocation(
                    builder.getLocation().x+(builder.getWidth() - preview.getWidth()) / 2,
                    builder.getLocation().y+(builder.getHeight() - preview.getHeight()) / 3);
            preview.setModal(true);
            preview.getContentPane().add(previewPanel);
            preview.setVisible(true);


        } catch (JDOMException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
