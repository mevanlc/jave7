package de.jave.figlet.swing.fontchooser;

import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.ui.FigletIcons;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CategoriesTreeCellRenderer extends DefaultTreeCellRenderer {
   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      if (!(value instanceof DefaultMutableTreeNode)) {
         throw new IllegalStateException();
      } else {
         Object object = ((DefaultMutableTreeNode)value).getUserObject();
         if (!(object instanceof IFigFontCategory)) {
            return this;
         } else {
            IFigFontCategory category = (IFigFontCategory)object;
            this.setText(this.getDisplayName(category));
            this.setToolTipText(this.getToolTipText(category));
            Icon icon = null;
            if (leaf) {
               if (category.getFontCount() == 0) {
                  icon = FigletIcons.FONT_CATEGORY_ICON_DISABLED;
               } else {
                  icon = FigletIcons.FONT_CATEGORY_ICON;
               }
            } else if (expanded) {
               icon = FigletIcons.FOLDER_OPEN_ICON;
            } else {
               icon = FigletIcons.FOLDER_CLOSED_ICON;
            }

            this.setIcon(icon);
            return this;
         }
      }
   }

   private String getToolTipText(IFigFontCategory category) {
      String description = category.getDescription();
      return description != null && description.length() != 0 ? description : null;
   }

   private String getDisplayName(IFigFontCategory category) {
      int size = category.getFontCount();
      return size > 0 ? category.getName() + " (" + size + ")" : category.getName();
   }
}
