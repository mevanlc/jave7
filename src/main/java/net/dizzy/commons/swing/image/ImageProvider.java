package net.dizzy.commons.swing.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageProvider {
   private final String basePath;
   private static final ImageIcon FALLBACK_ICON = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

   public ImageProvider(String basePath) {
      this.basePath = normalizeBasePath(basePath);
   }

   public Image getImage(String name) {
      return getImageIcon(name).getImage();
   }

   public ImageIcon getImageIcon(String name) {
      URL url = getResource(name);
      return url == null ? FALLBACK_ICON : new ImageIcon(url);
   }

   public boolean hasImage(String name) {
      return getResource(name) != null;
   }

   private URL getResource(String name) {
      return Thread.currentThread().getContextClassLoader().getResource(basePath + name);
   }

   private static String normalizeBasePath(String value) {
      if (value == null || value.isEmpty()) {
         return "";
      }
      String normalized = value.startsWith("/") ? value.substring(1) : value;
      return normalized.endsWith("/") ? normalized : normalized + "/";
   }
}
