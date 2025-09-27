package net.disy.commons.swing.dialog;

import javax.swing.Icon;
import net.disy.commons.swing.image.ImageProvider;
import net.disy.commons.swing.resources.IIconResources;

public class DisyCommonsSwingDialogIconResources implements IIconResources {
   public static final Icon DIALOG_HEADER_ICON_BACKGROUND = getImageIcon("dialog_header_icon_background.gif");
   public static final Icon TAB_CLOSE_INACTIVE = getImageIcon("close_inactive.gif");
   public static final Icon TAB_CLOSE_ACTIVE = getImageIcon("close_active.gif");
   public static final Icon DIALOG_HELP = getImageIcon("dialog_help.gif");
   public static final Icon SEARCH_TEXTFIELD = getImageIcon("search_textfield.gif");

   private static Icon getImageIcon(String relativePath) {
      return new ImageProvider("net/disy/commons/swing/dialog/icons").getImageIcon(relativePath);
   }
}
