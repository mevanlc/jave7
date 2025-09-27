package net.disy.commons.swing.layout.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class LayoutUtilities {
   private static final int DEFAULT_SCREEN_RESOLUTION = 96;
   public static final Dimension TOOLBAR_BUTTON_SIZE = new Dimension(22, 21);
   private static final EmptyBorder DEFAULT_EMPTY_BORDER = new EmptyBorder(getDpiAdjusted(5), getDpiAdjusted(6), getDpiAdjusted(5), getDpiAdjusted(6));

   public static int getScreenResolution() {
      try {
         return Toolkit.getDefaultToolkit().getScreenResolution();
      } catch (Exception var1) {
         return 96;
      }
   }

   public static int getComponentSpacing() {
      return getComponentSpacing(getScreenResolution());
   }

   public static int getComponentSpacing(int screenResolutionInDpi) {
      return getDpiAdjusted(6, screenResolutionInDpi);
   }

   public static int getDpiAdjusted(int pixels, int screenResolutionInDpi) {
      return pixels * screenResolutionInDpi / 96;
   }

   public static int getDpiAdjusted(int pixels) {
      return pixels * getScreenResolution() / 96;
   }

   public static int getComponentGroupsSpacing() {
      return getComponentGroupsSpacing(getScreenResolution());
   }

   public static int getComponentGroupsSpacing(int screenResolutionInDpi) {
      return getDpiAdjusted(11, screenResolutionInDpi);
   }

   public static Border getDefaultEmptyBorder() {
      return DEFAULT_EMPTY_BORDER;
   }

   public static BorderLayout createDefaultBorderLayout() {
      return new BorderLayout(getComponentSpacing(), getComponentSpacing());
   }
}
