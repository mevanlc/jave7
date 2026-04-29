package de.jave.jave;

import java.awt.Dimension;

public final class DocumentSizeLimits {
   public static final int MIN_WIDTH = 1;
   public static final int MIN_HEIGHT = 1;
   public static final int MAX_WIDTH = 2048;
   public static final int MAX_HEIGHT = 1024;
   public static final Dimension MAX_SIZE = new Dimension(MAX_WIDTH, MAX_HEIGHT);

   private DocumentSizeLimits() {
   }

   public static int clampWidth(int width) {
      return Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, width));
   }

   public static int clampHeight(int height) {
      return Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, height));
   }
}
