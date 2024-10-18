package de.jave.figlet.swing.ui;

import de.jave.figlet.file.IFigFontCategory;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class FontCategoriesListCellRenderer extends DefaultListCellRenderer {
   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      if (value instanceof IFigFontCategory) {
         IFigFontCategory category = (IFigFontCategory)value;
         this.setText(category.getName());
         this.setToolTipText(category.getDescription());
         if (category.isDynamicallyGenerated()) {
            this.setIcon(FigletIcons.DYNAMICALY_GENERATED_FONT_CATEGORY_ICON);
         } else {
            this.setIcon(FigletIcons.FONT_CATEGORY_ICON);
         }
      }

      return this;
   }
}
