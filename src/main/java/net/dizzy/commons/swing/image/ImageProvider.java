package net.dizzy.commons.swing.image;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageProvider {
   private final String basePath;

   public ImageProvider(String basePath) {
      this.basePath = basePath == null ? "" : basePath;
   }

   public Image getImage(String name) {
      return getImageIcon(name).getImage();
   }

   public ImageIcon getImageIcon(String name) {
      URL url = getClass().getResource(basePath + name);
      if (url == null) {
         url = Thread.currentThread().getContextClassLoader().getResource(trimSlash(basePath + name));
      }
      return url == null ? new ImageIcon() : new ImageIcon(url);
   }

   private static String trimSlash(String value) {
      return value.startsWith("/") ? value.substring(1) : value;
   }
}
