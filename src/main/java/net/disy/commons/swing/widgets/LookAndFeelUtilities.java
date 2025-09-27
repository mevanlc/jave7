package net.disy.commons.swing.laf;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.disy.commons.swing.color.SwingColors;

public class LookAndFeelUtilities {
   public static final String COMPONENT_TYPE_LABEL = "Label";

   public static void installColorsAndFont(JComponent component, String type) {
      LookAndFeel.installColorsAndFont(component, type + ".background", type + ".foreground", type + ".font");
   }

   public static void adjustCell(JComponent renderer, JTable table, boolean isSelected, boolean hasFocus, boolean cellEditable) {
      if (isSelected) {
         renderer.setForeground(table.getSelectionForeground());
         renderer.setBackground(table.getSelectionBackground());
      } else {
         renderer.setForeground(table.getForeground());
         renderer.setBackground(table.getBackground());
      }

      if (hasFocus) {
         renderer.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
         if (cellEditable) {
            renderer.setForeground(SwingColors.getTableFocusCellForegroundColor());
            renderer.setBackground(SwingColors.getTableFocusCellBackgroundColor());
         }
      } else {
         renderer.setBorder(new EmptyBorder(1, 1, 1, 1));
      }
   }

   public static void setSystemLookAndFeel() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var1) {
      }
   }
}
