package net.disy.commons.swing.geometry;

import java.awt.Dimension;

public class DimensionUtilities {
   private DimensionUtilities() {
   }

   public static Dimension fitInto(Dimension content, Dimension container) {
      return fitInto(content.getWidth() / content.getHeight(), container);
   }

   public static Dimension fitInto(double contentRatio, Dimension container) {
      double containerRatio = container.getWidth() / container.getHeight();
      double factor = contentRatio / containerRatio;
      if (factor > 1.0) {
         factor = 1.0 / factor;
      }

      if (containerRatio < contentRatio) {
         return new Dimension(container.width, (int)((double)container.height * factor));
      } else {
         return containerRatio > contentRatio ? new Dimension((int)((double)container.width * factor), container.height) : container;
      }
   }
}
