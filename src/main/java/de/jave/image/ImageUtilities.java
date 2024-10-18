package de.jave.image;

import java.awt.Dimension;

public class ImageUtilities {
   public static Dimension calculateIsometricLimitedSize(Dimension originalSize, Dimension maxSize) {
      if (originalSize.width <= maxSize.width && originalSize.height <= maxSize.height) {
         return originalSize;
      } else {
         double scaleX = (double)maxSize.width / (double)originalSize.width;
         double scaleY = (double)maxSize.height / (double)originalSize.height;
         double scale = Math.min(scaleX, scaleY);
         int width = (int)(scale * (double)originalSize.width);
         int height = (int)(scale * (double)originalSize.height);
         return new Dimension(width, height);
      }
   }
}
