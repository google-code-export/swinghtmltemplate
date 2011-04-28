package ru.swing.html.builder;

import groovy.lang.GroovyClassLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Property;
import org.jdom.JDOMException;
import ru.swing.html.*;
import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Controller for editing panel. Holds internal Model for edited text.
 */
public class EditorPanel extends JPanel {

    private Log logger = LogFactory.getLog(getClass());

    @ModelElement("model")
    private Model model = new Model();

    public EditorPanel() {
        try {
            //we will substitute the texteditor from the markup with RSyntaxTextArea.
            //when we will create module for RSyntaxTextArea, we will remove substitutions
            HashMap<SelectorGroup, JComponent> substitutions = new HashMap<SelectorGroup, JComponent>();
            //html editor
            RSyntaxTextArea textArea = new RSyntaxTextArea();
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
            RTextScrollPane sp = new RTextScrollPane(textArea);
            substitutions.put(new SelectorGroup("#editorScroll"), sp);

            //code editor
            RSyntaxTextArea codeArea = new RSyntaxTextArea();
            codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
            sp = new RTextScrollPane(codeArea);
            substitutions.put(new SelectorGroup("#codeScroll"), sp);

            //bind controller to model
            DomModel model = Binder.bind(this, true, substitutions);
            //bind text from model with text from RSyntaxTextArea. Mannual binding cause we use substitutions
            model.bind("${model.text}", textArea, BeanProperty.create("text"));
            model.bind("${model.code}", codeArea, BeanProperty.create("text"));


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

    /**
     * Resets model.
     */
    public void reset() {
        model.reset();
    }

    /**
     * Builds PreviewPanel from the html in the editor. Creates modal dialog and
     * sets it visible.
     */
    public void build() {
        String html = model.getText();
        InputStream in = new ByteArrayInputStream(html.getBytes());
        try {
            DomModel model = DomLoader.loadModel(in);

            Object controller = null;
            GroovyClassLoader gcl = new GroovyClassLoader();
            if (StringUtils.isNotEmpty(this.model.getCode())) {
                Class clazz = gcl.parseClass(this.model.getCode());
                try {
                    controller = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Property<Object, DomModel> p = BeanProperty.create("model");
                if (p.isWriteable(controller)) {
                    p.setValue(controller, model);
                }
                Binder.injectModelElements(controller, model);
            }

            model.setController(controller);

            PreviewPanel previewPanel = new PreviewPanel();
            previewPanel.setModel(model);
            try {
                previewPanel.compose();
            } catch (Exception e) {
                logger.error("Can't create preview: "+e.getMessage(), e);
                return;
            }


            MainPanel parent = Builder.getInstance();
            JDialog preview = new JDialog(parent, "Preview");
            preview.setSize(800, 600);
            preview.setLocation(
                    parent.getLocation().x+(parent.getWidth() - preview.getWidth()) / 2,
                    parent.getLocation().y+(parent.getHeight() - preview.getHeight()) / 3);
            preview.setModal(true);
            preview.getContentPane().add(previewPanel);
            preview.setVisible(true);


        } catch (Exception e1) {
            e1.printStackTrace();
        } 
    }


    /**
     * Javabean model for holding internal data. Holds text and original text of the editor.
     */
    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String text;
        private String code;
        private String originalCode;
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
            setCode(originalCode);
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getOriginalCode() {
            return originalCode;
        }

        public void setOriginalCode(String originalCode) {
            this.originalCode = originalCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            String old = this.code;
            this.code = code;
            pcs.firePropertyChange("code", old, code);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }



}
