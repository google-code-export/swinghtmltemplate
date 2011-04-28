package ru.swing.html.builder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import ru.swing.html.Bind;
import ru.swing.html.Binder;
import ru.swing.html.ModelElement;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

/**
 * The controller for the main panel of the builder.
 */
public class MainPanel extends JFrame {

    @ModelElement("examplesModel")
    private TreeModel examplesModel;

    private Log logger = LogFactory.getLog(getClass());

    @Bind("toolWindowManager")
    private MyDoggyToolWindowManager toolWindowManager;


    public MainPanel() {
        //create examples tree model
        examplesModel = new ExamplesService().createTreeModel();
        try {
            Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Takes the clicked node in the examples tree. If user object of the node
     * is Example, creates new EditorPanel, loads html and adds panel to the mydoggy content.
     * @param e
     */
    public void onExampleOpen(MouseEvent e) {
        JTree tree = (JTree) e.getSource();
        //do nothing if no path is clicked
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path==null || path.getPathCount()<=0) {
            return;
        }

        //take clicked node
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node==null) {
            return;
        }
        else if (node.getUserObject() instanceof Example) {
            //if the node is example, open EditorPanel
            Example example = (Example) node.getUserObject();
            //get html address
            String url = example.getSource();
            logger.info("Opening "+ url);

            //create editor panel
            EditorPanel editorPanel = new EditorPanel();
            try {
                //load html
                if (StringUtils.isNotEmpty(example.getSource())) {
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(example.getSource());
                    String text = IOUtils.toString(inputStream);
                    inputStream.close();
                    editorPanel.getModel().setOriginal(text);
                }

                if (StringUtils.isNotEmpty(example.getCode())) {
                    //load groovy
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(example.getCode());
                    String code = null;
                    if (inputStream!=null) {
                        code = IOUtils.toString(inputStream);
                        inputStream.close();
                    }
                    editorPanel.getModel().setOriginalCode(code);
                }
                //init editor panel
                editorPanel.getModel().reset();
                //add content to the mydoggy
                toolWindowManager.getContentManager().addContent(example.getName(), example.getName(), null, editorPanel);
            } catch (Exception e1) {
                logger.error("Can't open source '"+url+"' for example '"+example.getName()+"'", e1);
            }



        }
    }
}
