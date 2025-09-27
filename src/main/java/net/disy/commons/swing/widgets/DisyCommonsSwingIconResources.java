package net.disy.commons.swing.resources;

import javax.swing.Icon;
import net.disy.commons.swing.image.ImageProvider;

public class DisyCommonsSwingIconResources implements IIconResources {
   public static final Icon UNDO = getImageIcon("undo.gif");
   public static final Icon REDO = getImageIcon("redo.gif");
   public static final Icon UNDO_MODERN = getImageIcon("undo_modern.gif");
   public static final Icon REDO_MODERN = getImageIcon("redo_modern.gif");
   public static final Icon COPY = getImageIcon("copy.gif");
   public static final Icon CUT = getImageIcon("cut.gif");
   public static final Icon PASTE = getImageIcon("paste.gif");

   private static Icon getImageIcon(String relativePath) {
      return new ImageProvider("net/disy/commons/swing/icons").getImageIcon(relativePath);
   }
}
