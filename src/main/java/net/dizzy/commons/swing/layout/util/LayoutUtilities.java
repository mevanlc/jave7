package net.dizzy.commons.swing.layout.util;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public final class LayoutUtilities {
   private LayoutUtilities() {
   }

   public static int getDpiAdjusted(int value) {
      return value;
   }

   public static int getComponentSpacing() {
      return 5;
   }

   public static int getComponentGroupsSpacing() {
      return 10;
   }

   public static Border getDefaultEmptyBorder() {
      return BorderFactory.createEmptyBorder(5, 5, 5, 5);
   }
}
