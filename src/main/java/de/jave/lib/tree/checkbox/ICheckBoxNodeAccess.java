package de.jave.lib.tree.checkbox;

import javax.swing.tree.TreeNode;

public interface ICheckBoxNodeAccess {
   boolean isCheckableNode(TreeNode var1);

   boolean isChecked(TreeNode var1);

   boolean isCheckEditable(TreeNode var1);

   void setChecked(TreeNode var1, boolean var2);
}
