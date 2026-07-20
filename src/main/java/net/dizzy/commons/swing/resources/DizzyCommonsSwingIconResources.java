package net.dizzy.commons.swing.resources;

import de.jave.preferences.JavePreferences;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.dizzy.commons.swing.icon.IconScaler;

public final class DizzyCommonsSwingIconResources implements IIconResources {
   private static final int ICON_SIZE = JavePreferences.readIconSizePreference();
   public static final Icon CUT = icon("cut");
   public static final Icon COPY = icon("copy");
   public static final Icon PASTE = icon("paste");
   public static final Icon UNDO_MODERN = icon("undo");
   public static final Icon REDO_MODERN = icon("redo");

   private DizzyCommonsSwingIconResources() {
   }

   private static Icon icon(String name) {
      URL url = DizzyCommonsSwingIconResources.class.getResource(name + ".gif");
      Icon base = url == null ? new ImageIcon() : new ImageIcon(url);
      return IconScaler.scaleToPreferredSize(base, ICON_SIZE);
   }
}
