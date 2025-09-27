package net.disy.commons.swing.table;

import javax.swing.Icon;

public class DefaultTableHeaderToolTipProvider implements ITableHeaderToolTipProvider {
   @Override
   public String getToolTip(Object value, int columnIndex) {
      return value == null ? "" : value.toString();
   }

   @Override
   public Icon getIcon(int columnIndex) {
      return null;
   }
}
