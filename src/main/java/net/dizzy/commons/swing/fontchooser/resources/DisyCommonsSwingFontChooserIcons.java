package net.dizzy.commons.swing.fontchooser.resources;

import de.jave.preferences.JavePreferences;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.dizzy.commons.swing.icon.IconScaler;

public final class DisyCommonsSwingFontChooserIcons {
   private static final int ICON_SIZE = JavePreferences.readIconSizePreference();
   public static final Icon FONT_ICON = icon("font");

   private DisyCommonsSwingFontChooserIcons() {
   }

   private static Icon icon(String name) {
      URL url = DisyCommonsSwingFontChooserIcons.class.getResource(name + ".gif");
      Icon base = url == null ? new ImageIcon() : new ImageIcon(url);
      return IconScaler.scaleToPreferredSize(base, ICON_SIZE);
   }
}
