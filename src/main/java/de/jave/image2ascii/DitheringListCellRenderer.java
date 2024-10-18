package de.jave.image2ascii;

import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class DitheringListCellRenderer extends DefaultListCellRenderer {
   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      this.setText(((IGreyscaleDithering)value).getName());
      return this;
   }
}
