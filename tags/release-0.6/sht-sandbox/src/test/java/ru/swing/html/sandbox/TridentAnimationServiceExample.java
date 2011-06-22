package ru.swing.html.sandbox;

import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Animation example
 */
public class TridentAnimationServiceExample extends JFrame {

    private DomModel model;

    public TridentAnimationServiceExample() throws JDOMException, IOException {
        model = Binder.bind(this, true);
    }

    public void onClick() {
        Tag label = model.getTagById("label");
        new TridentAnimationService().animate(label, "opacity", 1f, 0f, 2500);
    }


    public static void main(String[] args) throws JDOMException, IOException {
        final TridentAnimationServiceExample form = new TridentAnimationServiceExample();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                form.setVisible(true);
            }
        });
    }

}
