package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JList;

public class FontFamilyListCellRenderer extends AbstractFontListCellRenderer {
   public FontFamilyListCellRenderer(IFontDialogProperties properties) {
      super(properties);
   }

   @Override
   public final Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      Font font = new Font(value.toString(), 0, 12);
      component.setFont(this.getDisplayFont(font));
      return component;
   }
}
