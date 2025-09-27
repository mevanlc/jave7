package net.disy.commons.swing.ui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.disy.commons.core.util.Ensure;

public class ObjectUiTreeCellRenderer extends DefaultTreeCellRenderer {
   private final IObjectUi objectUi;

   public ObjectUiTreeCellRenderer(IObjectUi objectUi) {
      Ensure.ensureArgumentNotNull(objectUi);
      this.objectUi = objectUi;
   }

   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
      Icon icon = this.objectUi.getIcon(value);
      if (icon != null) {
         this.setIcon(icon);
      }

      this.setText(this.objectUi.getLabel(value));
      this.setToolTipText(this.objectUi.getToolTipText(value));
      return this;
   }
}
