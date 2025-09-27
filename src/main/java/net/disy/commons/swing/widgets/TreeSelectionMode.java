package net.disy.commons.swing.tree;

public class TreeSelectionMode {
   private final int treeSelectionMode;
   public static final TreeSelectionMode SINGLE_TREE_SELECTION = new TreeSelectionMode(1);
   public static final TreeSelectionMode DISCONTIGUOUS_TREE_SELECTION = new TreeSelectionMode(4);
   public static final TreeSelectionMode CONTIGUOUS_TREE_SELECTION = new TreeSelectionMode(2);

   private TreeSelectionMode(int treeSelectionMode) {
      this.treeSelectionMode = treeSelectionMode;
   }

   public int getTreeSelectionMode() {
      return this.treeSelectionMode;
   }
}
