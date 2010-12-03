package ru.swing.html.builder;

import org.jdom.JDOMException;
import ru.swing.html.*;

import javax.swing.*;
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
public class MainPanel {

    @Bind("build")
    private JButton buildBtn;
    @Bind("clear")
    private JButton clearBtn;
    @Bind("content")
    private JTextArea content;
    @Bind("rootPanel")
    private JPanel rootPanel;


    public MainPanel() {
        try {
            Binder.bind(this);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        buildBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String html = content.getText();
                InputStream in = new ByteArrayInputStream(html.getBytes());
                try {
                    DomModel model = DomLoader.loadModel(in);
                    DomConverter.toSwing(model);
                    JComponent root = model.getRootTag().getChildByName("body").getComponent();

                    Builder builder = Builder.getInstance();
                    JDialog preview = new JDialog(builder, "Preview");
                    preview.setSize(500, 400);
                    preview.setLocation(
                            builder.getLocation().x+(builder.getWidth() - preview.getWidth()) / 2,
                            builder.getLocation().y+(builder.getHeight() - preview.getHeight()) / 3);
                    preview.setModal(true);
                    preview.getContentPane().add(root);
                    preview.setVisible(true);


                } catch (JDOMException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                content.setText("");
            }
        });
    }


    public JPanel getRootPanel() {
        return rootPanel;
    }
}
