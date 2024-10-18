package de.jave.lib.tree.checkbox;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

public abstract class AbstractCheckboxTreeRenderer extends JPanel {
   protected CheckBoxTreeCheckbox checkBox;
   private Component label;
   private final ICheckBoxNodeAccess nodeAccess;

   protected AbstractCheckboxTreeRenderer(ICheckBoxNodeAccess nodeAccess) {
      this.nodeAccess = nodeAccess;
      this.setLayout(null);
      this.checkBox = new CheckBoxTreeCheckbox();
      this.setOpaque(false);
   }

   @Override
   public final Dimension getPreferredSize() {
      Dimension d_check = this.checkBox.getPreferredSize();
      Dimension d_label = this.label.getPreferredSize();
      return new Dimension(d_check.width + d_label.width, d_check.height < d_label.height ? d_label.height : d_check.height);
   }

   @Override
   public final void doLayout() {
      Dimension d_check = this.checkBox.getPreferredSize();
      Dimension d_label = this.label.getPreferredSize();
      int y_check = 0;
      int y_label = 0;
      if (d_check.height < d_label.height) {
         y_check = (d_label.height - d_check.height) / 2;
      } else {
         y_label = (d_check.height - d_label.height) / 2;
      }

      this.checkBox.setLocation(0, y_check);
      this.checkBox.setBounds(0, y_check, d_check.width, d_check.height);
      this.label.setLocation(d_check.width, y_label);
      this.label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
   }

   protected JComponent updateLabel(
      JTree tree, TreeCellRenderer defaultRenderer, TreeNode node, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus
   ) {
      this.label = null;
      this.removeAll();
      this.add(this.checkBox);
      this.label = defaultRenderer.getTreeCellRendererComponent(tree, node, isSelected, expanded, leaf, row, hasFocus);
      this.add(this.label);
      this.revalidate();
      return this;
   }

   protected CheckBoxNodeRenderState getRenderState(TreeNode node) {
      if (!this.getNodeAccess().isCheckableNode(node)) {
         throw new IllegalStateException();
      } else {
         boolean enabled = this.getNodeAccess().isCheckEditable(node);
         if (this.getNodeAccess().isChecked(node)) {
            return new CheckBoxNodeRenderState(VisibleCheckBoxState.SELECTED, enabled);
         } else {
            return this.isPartialChecked(node)
               ? new CheckBoxNodeRenderState(VisibleCheckBoxState.PARTIAL_SELECTED, enabled)
               : new CheckBoxNodeRenderState(VisibleCheckBoxState.NOT_SELECTED, enabled);
         }
      }
   }

   private boolean isPartialChecked(TreeNode node) {
      if (this.getNodeAccess().isCheckableNode(node) && this.getNodeAccess().isChecked(node)) {
         return true;
      } else {
         for (int i = 0; i < node.getChildCount(); i++) {
            if (this.isPartialChecked(node.getChildAt(i))) {
               return true;
            }
         }

         return false;
      }
   }

   protected ICheckBoxNodeAccess getNodeAccess() {
      return this.nodeAccess;
   }
}
