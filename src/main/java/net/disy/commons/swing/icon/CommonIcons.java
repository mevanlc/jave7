package net.disy.commons.swing.icon;

import javax.swing.Icon;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;
import net.disy.commons.swing.resources.IIconResources;

public class CommonIcons implements IIconResources {
   public static final Icon DELETE = getImageIcon("common/delete.gif");
   public static final Icon DELETE_ALL = getImageIcon("common/delete_all.gif");
   public static final Icon REFRESH = getImageIcon("common/refresh.gif");
   public static final Icon PRINTER = getImageIcon("common/printer.gif");
   public static final Icon FOLDER = getImageIcon("file/folder.gif");
   public static final Icon FOLDER_NEW = getImageIcon("file/new_folder.gif");
   public static final Icon FOLDER_UP = getImageIcon("file/up_folder.gif");
   public static final Icon REFRESH_ANIMATION = DisyCommonsSwingImageProvider.getInstance().getAnimatedImageIcon("common/refresh_animation.gif");

   private static Icon getImageIcon(String name) {
      return DisyCommonsSwingImageProvider.getInstance().getImageIcon(name);
   }
}
