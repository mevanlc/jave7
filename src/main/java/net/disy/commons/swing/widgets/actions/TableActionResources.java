package net.disy.commons.swing.smarttable.actions;

import javax.swing.Icon;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.swing.image.ImageProvider;

public class TableActionResources {
   public static final Icon ADD_ROW_ICON = getImageIcon("add.png");
   public static final Icon DELETE_ROW_ICON = getImageIcon("remove.png");

   private TableActionResources() {
      throw new UnreachableCodeReachedException();
   }

   private static Icon getImageIcon(String relativePath) {
      return new ImageProvider("net/disy/commons/swing/smarttable/icons").getImageIcon(relativePath);
   }
}
