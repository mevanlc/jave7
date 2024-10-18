package de.jave.lib.tree.checkbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class CheckBoxCheckListener implements ActionListener {
   private final ICheckBoxNodeAccess nodeAccess;
   private TreeNode nodeToCheck;
   private JTree tree;
   private final CheckBoxSelectionMode mode;
   private final CheckboxTreeEditor editor;

   public CheckBoxCheckListener(ICheckBoxNodeAccess nodeAccess, CheckBoxSelectionMode mode, CheckboxTreeEditor editor) {
      this.editor = editor;
      this.nodeAccess = nodeAccess;
      this.mode = mode;
      this.nodeToCheck = null;
   }

   public void setNodeToCheck(TreeNode node) {
      this.nodeToCheck = node;
   }

   public void setTree(JTree tree) {
      this.tree = tree;
   }

   @Override
   public void actionPerformed(ActionEvent evt) {
      if (this.nodeToCheck != null) {
         if (this.mode == CheckBoxSelectionMode.SINGLE_SELECTION) {
            this.nodeAccess.setChecked(this.nodeToCheck, !this.nodeAccess.isChecked(this.nodeToCheck));
         } else if (this.nodeAccess.isChecked(this.nodeToCheck)) {
            this.selectWithChildren(this.nodeToCheck, false);
            this.uncheckAllParents(this.nodeToCheck.getParent());
         } else {
            this.selectWithChildren(this.nodeToCheck, true);
            this.checkAllParents(this.nodeToCheck.getParent());
         }

         this.editor.cancelCellEditing();
         ((DefaultTreeModel)this.tree.getModel()).nodeChanged((TreeNode)this.tree.getModel().getRoot());
      }
   }

   private void checkAllParents(TreeNode node) {
      if (node != null) {
         if (this.isAllChildrenChecked(node)) {
            if (this.nodeAccess.isCheckableNode(node)) {
               this.nodeAccess.setChecked(node, true);
            }

            this.checkAllParents(node.getParent());
         }
      }
   }

   private boolean isAllChildrenChecked(TreeNode node) {
      for (int i = 0; i < node.getChildCount(); i++) {
         TreeNode child = node.getChildAt(i);
         if (this.nodeAccess.isCheckableNode(child) && !this.nodeAccess.isChecked(child)) {
            return false;
         }
      }

      return true;
   }

   private void uncheckAllParents(TreeNode parent) {
      if (parent != null && this.nodeAccess.isCheckableNode(parent) && this.nodeAccess.isChecked(parent)) {
         this.nodeAccess.setChecked(parent, false);
         this.uncheckAllParents(parent.getParent());
      }
   }

   private void selectWithChildren(TreeNode node, boolean select) {
      if (this.nodeAccess.isCheckableNode(node)) {
         this.nodeAccess.setChecked(node, select);
      }

      for (int i = 0; i < node.getChildCount(); i++) {
         this.selectWithChildren(node.getChildAt(i), select);
      }
   }
}
