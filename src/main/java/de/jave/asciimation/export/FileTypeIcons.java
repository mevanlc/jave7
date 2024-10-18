package de.jave.asciimation.export;

import de.jave.jave.application.resources.JaveImageProvider;
import javax.swing.Icon;
import net.disy.commons.swing.resources.IIconResources;

public class FileTypeIcons implements IIconResources {
   public static final Icon ANIMATION_ICON = JaveImageProvider.getInstance().getImageIcon("animation.gif");
   public static final Icon TEXT_ICON = JaveImageProvider.getInstance().getImageIcon("text.gif");
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
      return JaveImageProvider.getInstance().getImageIcon("filetypes/" + name);
   }
}
