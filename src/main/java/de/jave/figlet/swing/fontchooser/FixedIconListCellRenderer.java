package de.jave.figlet.swing.fontchooser;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

public class FixedIconListCellRenderer extends DefaultListCellRenderer {
   private final Icon icon;

   public FixedIconListCellRenderer(Icon icon) {
      this.icon = icon;
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      this.setIcon(this.icon);
      return this;
   }
}
