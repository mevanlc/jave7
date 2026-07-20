package de.jave.figlet.swing.ui;

import de.jave.preferences.JavePreferences;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.dizzy.commons.swing.icon.IconScaler;

public class Resources {
   private static final Icon errorIcon = new Resources.ErrorIcon();
   private static final int ICON_SIZE = JavePreferences.readIconSizePreference();

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
      URL url = clazz.getResource(name);
      if (url == null) {
         System.err.println("Warning: could not load icon '" + name + "'.");
         return errorIcon;
      }

      try {
         // The 24px/32px variants are generated on the fly from the native ~16px art via the
         // shared IconScaler, instead of being packaged as pregenerated sized PNGs.
         return IconScaler.scaleToPreferredSize(new ImageIcon(url), ICON_SIZE);
      } catch (Exception var4) {
         return new Resources.ErrorIcon();
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
