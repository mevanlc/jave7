package net.disy.commons.swing.mousecursor;

import java.awt.Dimension;
import java.awt.Toolkit;

public class CursorCapabilities {
   public static final CursorCapabilities DIMENSION_16_DEPTH_2 = new CursorCapabilities(true, new Dimension(16, 16), 2);
   public static final CursorCapabilities DIMENSION_32_DEPTH_2 = new CursorCapabilities(true, new Dimension(32, 32), 2);
   public static final CursorCapabilities DIMENSION_64_DEPTH_2 = new CursorCapabilities(true, new Dimension(64, 64), 2);
   public static final CursorCapabilities DIMENSION_16_DEPTH_16 = new CursorCapabilities(true, new Dimension(16, 16), 16);
   public static final CursorCapabilities DIMENSION_32_DEPTH_16 = new CursorCapabilities(true, new Dimension(32, 32), 16);
   public static final CursorCapabilities DIMENSION_64_DEPTH_16 = new CursorCapabilities(true, new Dimension(64, 64), 16);
   public static final CursorCapabilities NO_CUSTOM_CURSORS_SUPPORTED = new CursorCapabilities(false, new Dimension(0, 0), 0);
   private static final CursorCapabilities[] ALL_SUPPORTED_CAPABILITIES = new CursorCapabilities[]{
      DIMENSION_16_DEPTH_16, DIMENSION_16_DEPTH_2, DIMENSION_32_DEPTH_16, DIMENSION_32_DEPTH_2, DIMENSION_64_DEPTH_16, DIMENSION_64_DEPTH_2
   };
   private final boolean customCursorsSupported;
   private final int maxColorDepth;
   private final Dimension bestCursorSize;

   private CursorCapabilities(boolean customCursorsSupported, Dimension bestCursorSize, int maxColorDepth) {
      this.customCursorsSupported = customCursorsSupported;
      this.bestCursorSize = bestCursorSize;
      this.maxColorDepth = maxColorDepth;
   }

   public static CursorCapabilities getSystemCapabilities() {
      int maxColorDepth = 0;
      Dimension bestCursorSize = null;
      int var5;
      try {
         Toolkit tk = Toolkit.getDefaultToolkit();
         maxColorDepth = tk.getMaximumCursorColors();
         if (maxColorDepth >= 16) {
            var5 = 16;
         } else {
            if (maxColorDepth < 2) {
               return NO_CUSTOM_CURSORS_SUPPORTED;
            }

            var5 = 2;
         }

         bestCursorSize = tk.getBestCursorSize(32, 32);
      } catch (Exception var3) {
         return NO_CUSTOM_CURSORS_SUPPORTED;
      }

      return bestCursorSize != null && bestCursorSize.width > 0 && bestCursorSize.height > 0
         ? getBestFittingSupportedCapability(var5, bestCursorSize)
         : NO_CUSTOM_CURSORS_SUPPORTED;
   }

   private static CursorCapabilities getBestFittingSupportedCapability(int maxColorDepth, Dimension bestCursorSize) {
      int bestIndex = -1;
      double bestDistance = 0.0;

      for (int i = 0; i < ALL_SUPPORTED_CAPABILITIES.length; i++) {
         if (ALL_SUPPORTED_CAPABILITIES[i].getMaxColorDepth() == maxColorDepth
            && (bestIndex == -1 || bestDistance > getDistance(ALL_SUPPORTED_CAPABILITIES[i].getBestCursorSize(), bestCursorSize))) {
            bestDistance = getDistance(ALL_SUPPORTED_CAPABILITIES[i].getBestCursorSize(), bestCursorSize);
            bestIndex = i;
         }
      }

      return bestIndex != -1 ? ALL_SUPPORTED_CAPABILITIES[bestIndex] : NO_CUSTOM_CURSORS_SUPPORTED;
   }

   private static double getDistance(Dimension d1, Dimension d2) {
      return Math.pow(d2.width - d1.width, 2.0) + Math.pow(d2.height - d1.height, 2.0);
   }

   public Dimension getBestCursorSize() {
      return this.bestCursorSize;
   }

   public boolean isCustomCursorsSupported() {
      return this.customCursorsSupported;
   }

   public int getMaxColorDepth() {
      return this.maxColorDepth;
   }

   public static CursorCapabilities[] getAll() {
      return ALL_SUPPORTED_CAPABILITIES;
   }
}
