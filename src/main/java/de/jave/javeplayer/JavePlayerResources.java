package de.jave.javeplayer;

import javax.swing.Icon;
import net.disy.commons.swing.image.ImageProvider;
import net.disy.commons.swing.resources.IIconResources;

public class JavePlayerResources implements IIconResources {
   public static final Icon REVERSE_ICON = getIcon("reverse.gif");
   public static final Icon FORWARD_ICON = getIcon("forward.gif");
   public static final Icon PLAY_ICON = getIcon("play.gif");
   public static final Icon STOP_ICON = getIcon("stop.gif");
   public static final Icon PAUSE_ICON = getIcon("pause.gif");
   public static final Icon ZOOM_PLUS_ICON = getIcon("zoomplus.gif");
   public static final Icon ZOOM_MINUS_ICON = getIcon("zoomminus.gif");
   public static final Icon LOOP_ICON = getIcon("loop.gif");
   public static final Icon REVERSE_FIRST_ICON = getIcon("reverse_first.gif");
   public static final Icon FORWARD_LAST_ICON = getIcon("forward_last.gif");

   private static Icon getIcon(String path) {
      ImageProvider imageProvider = new ImageProvider("de/jave/jmov/player");
      return imageProvider.getImageIcon(path);
   }
}
