package de.jave.asciimation.export;

import de.jave.jave.application.resources.JaveImageProvider;
import de.jave.preferences.JavePreferences;
import javax.swing.Icon;
import net.dizzy.commons.swing.icon.IconScaler;
import net.dizzy.commons.swing.resources.IIconResources;

public class FileTypeIcons implements IIconResources {
   private static final int ICON_SIZE = JavePreferences.readIconSizePreference();
   public static final Icon ANIMATION_ICON = loadIcon("animation.gif");
   public static final Icon TEXT_ICON = loadIcon("text.gif");
   public static final Icon JAVASCRIPT_ICON = loadFileTypeIcon("javascript.gif");
   public static final Icon JAVA_ICON = loadFileTypeIcon("java.gif");
   public static final Icon TEXTS_ICON = loadFileTypeIcon("texts.gif");
   public static final Icon SCROLLBAR_ANIMATION_ICON = loadFileTypeIcon("scrollbaranimation.gif");
   public static final Icon ACTION_SCRIPT_ICON = loadFileTypeIcon("actionscript.gif");
   public static final Icon SWF_ICON = loadFileTypeIcon("swf.gif");
   public static final Icon GIF_ICON = loadFileTypeIcon("gif.gif");
   public static final Icon MULTIPLE_GIF_ICON = loadFileTypeIcon("gifs.gif");
   public static final Icon ANIMATED_GIF_ICON = loadFileTypeIcon("animatedgif.gif");

   private static final Icon loadFileTypeIcon(String name) {
      return loadIcon("filetypes/" + name);
   }

   private static Icon loadIcon(String path) {
      return IconScaler.scaleToPreferredSize(JaveImageProvider.getInstance().getImageIcon(path), ICON_SIZE);
   }
}
