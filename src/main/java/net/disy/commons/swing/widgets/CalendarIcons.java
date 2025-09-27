package net.disy.commons.swing.calendar;

import javax.swing.Icon;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;

public class CalendarIcons {
   public static final Icon DATE_ICON = getImageIcon("date.gif");

   private static Icon getImageIcon(String name) {
      return DisyCommonsSwingImageProvider.getInstance().getImageIcon("date/" + name);
   }
}
