package ru.swing.html.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.*;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
public class EditorPanel extends JPanel {

    private Log logger = LogFactory.getLog(getClass());

    @ModelElement("model")
    private Model model = new Model();

    public EditorPanel() {
        try {
            Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void reset() {
        model.reset();
    }

    public void build() {
        String html = model.getText();
        InputStream in = new ByteArrayInputStream(html.getBytes());
        try {
            DomModel model = DomLoader.loadModel(in);

            PreviewPanel previewPanel = new PreviewPanel();
            previewPanel.setModel(model);
            try {
                previewPanel.compose();
            } catch (Exception e) {
                logger.error("Can't create preview: "+e.getMessage(), e);
                return;
            }


            Builder builder = Builder.getInstance();
            JDialog preview = new JDialog(builder, "Preview");
            preview.setSize(800, 600);
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


    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String text;
        private String original;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            String old = this.text;
            this.text = text;
            pcs.firePropertyChange("text", old, text);
        }

        public void reset() {
            setText(original);
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }



}
