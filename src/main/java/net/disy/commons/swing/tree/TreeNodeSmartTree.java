package net.disy.commons.swing.tree;

import javax.swing.tree.TreeNode;

public class TreeNodeSmartTree implements ISmartTree<TreeNode> {
   private final TreeNode root;

   public TreeNodeSmartTree(TreeNode root) {
      this.root = root;
   }

   public TreeNode getChild(TreeNode parent, int index) {
      return parent.getChildAt(index);
   }

   public int getChildCount(TreeNode parent) {
      return parent.getChildCount();
   }

   public int getIndexOfChild(TreeNode parent, TreeNode child) {
      return parent.getIndex(child);
   }

   public TreeNode getRoot() {
      return this.root;
   }

   public boolean isLeaf(TreeNode node) {
      return !node.getAllowsChildren();
   }

   @Override
   public Class<TreeNode> getNodeClass() {
      return TreeNode.class;
   }
}
