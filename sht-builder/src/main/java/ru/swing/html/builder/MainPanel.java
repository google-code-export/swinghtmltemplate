package ru.swing.html.builder;

import org.apache.commons.io.IOUtils;
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
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 26.04.11
 * Time: 13:53
 */
public class MainPanel extends JPanel {

    @ModelElement("examplesModel")
    private TreeModel examplesModel;

    private Log logger = LogFactory.getLog(getClass());

    @Bind("toolWindowManager")
    private MyDoggyToolWindowManager toolWindowManager;


    public MainPanel() {
        examplesModel = new ExamplesService().createTreeModel();
        try {
            Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onExampleOpen(MouseEvent e) {
        JTree tree = (JTree) e.getSource();
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path==null || path.getPathCount()<=0) {
            return;
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node==null) {
            return;
        }
        else if (node.getUserObject() instanceof Example) {
            Example example = (Example) node.getUserObject();
            String url = example.getSource();
            logger.info("Opening "+ url);

            EditorPanel editorPanel = new EditorPanel();
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("examples/loginform/LoginForm.html");
                String text = IOUtils.toString(inputStream);
                editorPanel.getModel().setOriginal(text);
                editorPanel.getModel().reset();
                toolWindowManager.getContentManager().addContent(example.getName(), example.getName(), null, editorPanel);
            } catch (Exception e1) {
                logger.error("Can't open source '"+url+"' for example '"+example.getName()+"'", e1);
            }



        }
    }
}
