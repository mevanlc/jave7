package net.disy.commons.swing.smarttable.columnsettings.preferredwidth;

import javax.swing.JTextField;

public class TextPreferredWidth implements IPreferredWidth {
   private final int preferredWidth;

   public TextPreferredWidth(int columnCount) {
      this.preferredWidth = new JTextField(columnCount).getPreferredSize().width;
   }

   @Override
   public int getPreferredWidth() {
      return this.preferredWidth;
   }
}
