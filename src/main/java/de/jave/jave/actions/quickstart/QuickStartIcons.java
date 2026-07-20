package de.jave.jave.actions.quickstart;

import de.jave.jave.application.resources.JaveImageProvider;
import de.jave.preferences.JavePreferences;
import javax.swing.Icon;
import net.dizzy.commons.swing.icon.IconScaler;
import net.dizzy.commons.swing.resources.IIconResources;

public class QuickStartIcons implements IIconResources {
   private static final int ICON_SIZE = JavePreferences.readIconSizePreference();
   public static final Icon ACTION_ICON = loadIcon("quickstart.gif");
   public static final Icon LARGE_DIALOG_ICON = loadIcon("quickstart_large_dialog_icon.gif");
   public static final Icon ITEM_TEXT_ICON = loadIcon("item_text_editor.gif");
   public static final Icon ITEM_ANIMATION_ICON = loadIcon("item_animation_editor.gif");
   public static final Icon ITEM_WATERMARK_ICON = loadIcon("item_watermark.gif");
   public static final Icon ITEM_FIGLET_ICON = loadIcon("item_figlet.gif");
   public static final Icon ITEM_IMAGE2ASCII_ICON = loadIcon("item_image2ascii.gif");

   private static Icon loadIcon(String string) {
      return IconScaler.scaleToPreferredSize(JaveImageProvider.getInstance().getImageIcon("quickstart/" + string), ICON_SIZE);
   }
}
