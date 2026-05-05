package de.jave.figlet.swing.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.disy.commons.swing.icon.BaseIconImageIcon;

public class Resources {
   private static final Icon errorIcon = new Resources.ErrorIcon();
   private static final String PREFERENCES_NODE = "JavE";
   private static final String ICON_SIZE_KEY = "iconSize";
   private static final int DEFAULT_ICON_SIZE = 16;
   private static final int[] FALLBACK_ICON_SIZES = {32, 24};

   public static Icon getIconResource(String name) {
      return getIconResource(Resources.class, name);
   }

   public static URL getResourceUrl(String name) {
      return getResourceUrl(Resources.class, name);
   }

   public static URL getResourceUrl(Class clazz, String name) {
      return clazz.getResource(name);
   }

   private static Icon getIconResource(Class clazz, String name) {
      URL url = getPreferredResourceUrl(clazz, name);
      if (url == null) {
         System.err.println("Warning: could not load icon '" + name + "'.");
         return errorIcon;
      } else {
         try {
            URL baseUrl = clazz.getResource(name);
            return new BaseIconImageIcon(url, baseUrl == null ? null : new ImageIcon(baseUrl));
         } catch (Exception var4) {
            return new Resources.ErrorIcon();
         }
      }
   }

   private static URL getPreferredResourceUrl(Class clazz, String name) {
      int iconSize = readPreferredIconSize();
      if (iconSize != DEFAULT_ICON_SIZE) {
         int slash = name.lastIndexOf('/');
         int dot = name.lastIndexOf('.');
         if (dot > slash) {
            String dirPrefix = slash < 0 ? "" : name.substring(0, slash + 1);
            String iconName = name.substring(slash + 1, dot);
            for (int size : FALLBACK_ICON_SIZES) {
               if (size > iconSize) {
                  continue;
               }
               URL url = clazz.getResource(dirPrefix + size + "/" + iconName + ".png");
               if (url != null) {
                  return url;
               }
            }
         }
      }
      return clazz.getResource(name);
   }

   private static int readPreferredIconSize() {
      try {
         int value = Preferences.userRoot().node(PREFERENCES_NODE).getInt(ICON_SIZE_KEY, DEFAULT_ICON_SIZE);
         return value == 24 || value == 32 ? value : DEFAULT_ICON_SIZE;
      } catch (Exception e) {
         return DEFAULT_ICON_SIZE;
      }
   }

   private static final class ErrorIcon implements Icon {
      private ErrorIcon() {
      }

      @Override
      public int getIconWidth() {
         return 16;
      }

      @Override
      public int getIconHeight() {
         return 16;
      }

      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
         Color previousColor = g.getColor();
         g.setColor(Color.red);
         g.fillRect(x, y, 16, 16);
         g.setColor(previousColor);
      }
   }
}
