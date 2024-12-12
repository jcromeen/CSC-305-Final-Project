import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

class DirectoryManager {
    private final DefaultMutableTreeNode srcNode; // Root node for the directory tree
    private final DefaultTreeModel treeModel;    // Tree model to manage the tree structure
    private final JTree directoryTree;           // UI component for the directory tree

    public DirectoryManager() {
        // Initialize the root as a directory
        srcNode = new DefaultMutableTreeNode("src");
        treeModel = new DefaultTreeModel(srcNode);
        directoryTree = new JTree(treeModel);
    }

    public JTree getDirectoryTree() {
        return directoryTree;
    }

    // Adds a new Java file to the directory tree under the "src" node
    public void addBoxToDirectory(String boxName) {
        DefaultMutableTreeNode newFileNode = new DefaultMutableTreeNode(boxName + ".java");
        srcNode.add(newFileNode); // Add new file to the directory
        treeModel.reload();       // Notify the tree model of changes
    }

    public void renameBoxInDirectory(String oldName, String newName) {
        for (int i = 0; i < srcNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) srcNode.getChildAt(i);
            if (child.getUserObject().toString().equals(oldName + ".java")) {
                child.setUserObject(newName + ".java"); // Rename file in the directory
                treeModel.reload(); // Notify the tree model of changes
                return;
            }
        }
    }

    public void removeBoxFromDirectory(String boxName) {
        for (int i = 0; i < srcNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) srcNode.getChildAt(i);
            if (child.getUserObject().toString().equals(boxName + ".java")) {
                srcNode.remove(child); // Remove file from the directory
                treeModel.reload(); // Notify the tree model of changes
                return;
            }
        }
    }

    public String getSelectedFileName() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) directoryTree.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.isRoot()) {
            return null; // No file selected or root node selected
        }
        return selectedNode.getUserObject().toString();
    }

    public void clearDirectory() {
        srcNode.removeAllChildren(); // Remove all child nodes
        treeModel.reload();          // Notify the tree model of changes
    }
}
