package ru.swing.html.mydoggy.imagebrowser;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class FolderTreeNode extends LazyMutableTreeNode {


    public FolderTreeNode(File folder) {
        super(folder);
    }

    private static FileFilter folderFilter = new FileFilter() {
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.isHidden();
        }
    };

    @Override
    protected void initChildren() {
        File dir = (File) getUserObject();
        File[] subfolders = dir.listFiles(folderFilter);
        if (subfolders!=null) {
            Arrays.sort(subfolders);
            for (File f : subfolders) {
                FolderTreeNode node = new FolderTreeNode(f);
                add(node);
            }
        }
    }
}
