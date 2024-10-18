package de.jave.lib.tree.checkbox;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

public class CheckboxTreeRenderer extends AbstractCheckboxTreeRenderer implements TreeCellRenderer {
   private final TreeCellRenderer defaultRenderer;

   public CheckboxTreeRenderer(ICheckBoxNodeAccess nodeAccess, TreeCellRenderer defaultRenderer) {
      super(nodeAccess);
      if (defaultRenderer == null) {
         defaultRenderer = new DefaultTreeCellRenderer();
      }

      this.defaultRenderer = defaultRenderer;
   }

   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      if (value instanceof TreeNode && this.getNodeAccess().isCheckableNode((TreeNode)value)) {
         TreeNode node = (TreeNode)value;
         boolean treeIsEnabled = tree.isEnabled();
         boolean nodeIsEnabled = this.getNodeAccess().isCheckEditable(node);
         boolean isEnabled = treeIsEnabled && nodeIsEnabled;
         this.setEnabled(isEnabled);
         this.checkBox.setState(this.getRenderState(node));
         this.checkBox.setEnabled(isEnabled);
         return this.updateLabel(tree, this.defaultRenderer, node, isSelected, expanded, leaf, row, hasFocus);
      } else {
         return this.defaultRenderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
      }
   }
}
