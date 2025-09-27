package net.disy.commons.swing.tree;

import java.util.LinkedList;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class TreeModelUtilities {
   public static TreeNode[] getPathToRoot(TreeModel treeModel, TreeNode node) {
      LinkedList<TreeNode> path = new LinkedList<>();

      while (node != treeModel.getRoot()) {
         if (node == null) {
            return null;
         }

         path.addFirst(node);
         node = node.getParent();
      }

      path.addFirst(node);
      return path.toArray(new TreeNode[0]);
   }

   public static boolean isAncestorOrSame(TreeNode possibleAncestor, TreeNode possibleDescendant) {
      for (TreeNode actualNode = possibleDescendant; actualNode != null; actualNode = actualNode.getParent()) {
         if (actualNode == possibleAncestor) {
            return true;
         }
      }

      return false;
   }
}
