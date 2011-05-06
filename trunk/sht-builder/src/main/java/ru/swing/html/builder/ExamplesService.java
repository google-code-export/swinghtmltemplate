package ru.swing.html.builder;

import org.apache.commons.lang.StringUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * Service for examples. Examples are initiated in the constructor.
 * Contains methods for converting examples list into TreeModel.
 */
public class ExamplesService {

    private List<Example> examples;
    private Map<String[], List<Example>> examplesByPath;

    public ExamplesService() {

        examples = new ArrayList<Example>();
        //init examples list
        examples.add(new Example("'Create project' form", "examples/createproject/CreateProjectForm.html",
                "examples/createproject/CreateProjectForm.groovy", new String[] {"Forms"}));
        examples.add(new Example("Login form", "examples/loginform/LoginForm.html",
                "examples/loginform/LoginForm.groovy", new String[] {"Forms"}));
        examples.add(new Example("Matrix binded to formTable", "examples/matrixformtable/MatrixFormTable.html",
                "examples/matrixformtable/MatrixFormTable.groovy", new String[] {"Tags"}));

        //group examples by path. Key - path. Value - list of the examples
        examplesByPath = new HashMap<String[], List<Example>>();
        for (Example pt : examples) {
            String[] path = pt.getPath();
            List<Example> exampleList;
            if (examplesByPath.containsKey(path)) {
                exampleList = examplesByPath.get(path);
            }
            else {
                exampleList = new ArrayList<Example>();
                examplesByPath.put(path, exampleList);
            }
            exampleList.add(pt);
        }


    }

    /**
     * Returns the examples, licated in the path
     * @param path path of the examples
     * @return list of the examples
     */
    public List<Example> getExamplesByPath(String[] path) {
        TreePath param = new TreePath(path);
        //we must convert keys to the TreePath to enable .equals() (String[] do not supports it)
        List<Example> res = new ArrayList<Example>();
        for (String[] key : examplesByPath.keySet()) {
            TreePath pathKey = new TreePath(key);
            if (pathKey.equals(param)) {
                res.addAll(examplesByPath.get(key));
            }

        }
        return res;
    }


    /**
     * Creates TreeModel from the examples.
     * @return tree model
     */
    public TreeModel createTreeModel() {

        //group nodes by path to avoid simmular nodes.
        //e.g. if we have /foo/foo1 and /foo/foo2 paths, with griuping we will
        //forbid 2 nodes for /foo path.
        //also sort paths by name.
        Map<TreePath, DefaultMutableTreeNode> nodesByPath = new TreeMap<TreePath, DefaultMutableTreeNode>(new Comparator<TreePath>() {
            public int compare(TreePath o1, TreePath o2) {
                String path1 = StringUtils.join(o1.getPath());
                String path2 = StringUtils.join(o2.getPath());
                return path1.compareTo(path2);
            }
        });

        //fill groups
        for (String[] path : examplesByPath.keySet()) {
            // /foo/foo1 produces 2 nodes, /foo and /foo/foo1
            for (int i = 1; i<=path.length; i++) {

                String[] subPath = new String[i];
                System.arraycopy(path, 0, subPath, 0, subPath.length);
                TreePath treePath = new TreePath(subPath);

                if (!nodesByPath.containsKey(treePath)) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(subPath[subPath.length-1], true);
                    nodesByPath.put(treePath, node);
                }

            }
        }

        //create root node
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Projects");

        //append all nodes to the parents
        for (TreePath path : nodesByPath.keySet()) {
            DefaultMutableTreeNode node = nodesByPath.get(path);
            if (path.getPathCount()>1) {
                TreePath parentPath = path.getParentPath();
                DefaultMutableTreeNode parentNode = nodesByPath.get(parentPath);
                parentNode.add(node);
            }
            else {
                rootNode.add(node);
            }
        }

        //append nodes with the examples
        for (String[] path : examplesByPath.keySet()) {
            TreePath treePath = new TreePath(path);
            DefaultMutableTreeNode node = nodesByPath.get(treePath);
            List<Example> examples = examplesByPath.get(path);
            for (Example example : examples) {
                DefaultMutableTreeNode exampleNode = new DefaultMutableTreeNode(example);
                node.add(exampleNode);
            }
        }

        //create tree model
        TreeModel model = new DefaultTreeModel(rootNode);
        return model;
    }


}
