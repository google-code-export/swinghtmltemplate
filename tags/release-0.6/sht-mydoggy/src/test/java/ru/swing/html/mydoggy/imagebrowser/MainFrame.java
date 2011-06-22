package ru.swing.html.mydoggy.imagebrowser;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdom.JDOMException;
import ru.swing.html.Bind;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class MainFrame extends JFrame {

    @ModelElement("folders")
    private TreeModel folders;

    @ModelElement("folderRenderer")
    private FolderTreeRenderer folderRenderer = new FolderTreeRenderer();

    @ModelElement("imageRenderer")
    private ImageListRenderer imageRenderer = new ImageListRenderer();

    @Bind("foldersTree")
    private JTree foldersTree;

    @Bind("imagesList")
    private JList imagesList;

    @ModelElement("model")
    private Model model = new Model();


    public MainFrame() {

        File[] roots = File.listRoots();
        FolderTreeNode root = new FolderTreeNode(roots[0]);
        folders = new DefaultTreeModel(root);

        DomModel model = null;
        try {
            model = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //expand user's dir
        String userDir = System.getProperty("user.home");
        File homeDir = new File(userDir);
        gotoFolder(homeDir);

    }

    public void gotoFolder(File folder) {
        List<File> pathToRoot = new ArrayList<File>();
        File cur = folder;
        while (cur!=null) {
            pathToRoot.add(cur);
            cur = cur.getParentFile();
        }

        Collections.reverse(pathToRoot);
        //remove root
        pathToRoot.remove(0);
        TreePath path = new TreePath(foldersTree.getModel().getRoot());


        for (File file : pathToRoot) {
            FolderTreeNode founded = null;
            FolderTreeNode currentNode = (FolderTreeNode) path.getLastPathComponent();
            Enumeration children = currentNode.children();

            while (children.hasMoreElements() && founded==null) {
                FolderTreeNode node = (FolderTreeNode) children.nextElement();
                if (node.getUserObject().equals(file)) {
                    founded = node;
                    path = path.pathByAddingChild(founded);
                }
            }

            if (founded==null) {
                break;
            }

        }

        foldersTree.expandPath(path);
        foldersTree.setSelectionPath(path);

    }


    public void onThumbSizeChange() {
        imageRenderer.setThumbnailSize(model.getThumbnailSize());
        TreePath selectionPath = foldersTree.getSelectionPath();
        if (selectionPath!=null) {
            FolderTreeNode node = (FolderTreeNode) selectionPath.getLastPathComponent();
            File folder = (File) node.getUserObject();
            showImagesInFolder(folder);
        }


    }



    public void onFolderSelect(TreeSelectionEvent e) {
        TreePath selectionPath = e.getNewLeadSelectionPath();
        if (selectionPath!=null) {
            FolderTreeNode node = (FolderTreeNode) selectionPath.getLastPathComponent();
            File folder = (File) node.getUserObject();

            showImagesInFolder(folder);
        }

    }

    private void showImagesInFolder(File folder) {
        model.files.clear();

        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif");
            }
        });
        if (files!=null) {
            Arrays.sort(files);
            model.files.addAll(Arrays.asList(files));
        }
        model.setTotalText(model.files.size()+" images");
    }


    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }


    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String totalText;

        private int thumbnailSize = 100;

        private List<File> files = ObservableCollections.observableList(new ArrayList<File>());


        public List<File> getFiles() {
            return files;
        }

        public void setFiles(List<File> files) {
            this.files = files;
        }

        public String getTotalText() {
            return totalText;
        }

        public void setTotalText(String totalText) {
            String old = this.totalText;
            this.totalText = totalText;
            pcs.firePropertyChange("totalText", old, totalText);
        }

        public int getThumbnailSize() {
            return thumbnailSize;
        }

        public void setThumbnailSize(int thumbnailSize) {
            int old = this.thumbnailSize;
            this.thumbnailSize = thumbnailSize;
            pcs.firePropertyChange("thumbnailSize", old, thumbnailSize);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(propertyName, listener);
        }
    }

}
