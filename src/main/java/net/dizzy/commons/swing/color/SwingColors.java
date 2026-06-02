package net.dizzy.commons.swing.color;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.UIManager;

public final class SwingColors {
   private SwingColors() {
   }

   public static Color getControlDkShadowColor() {
      return color("controlDkShadow", SystemColor.controlDkShadow);
   }

   public static Color getControlLtHighlightColor() {
      return color("controlLtHighlight", SystemColor.controlLtHighlight);
   }

   public static Color getControlShadowColor() {
      return color("controlShadow", SystemColor.controlShadow);
   }

   public static Color getControlHighlightColor() {
      return color("controlHighlight", SystemColor.controlHighlight);
   }

   public static Color getTextAreaBackgroundColor() {
      return color("TextArea.background", Color.WHITE);
   }

   public static Color getTextAreaForegroundColor() {
      return color("TextArea.foreground", Color.BLACK);
   }

   public static Color getTextAreaInactiveForegroundColor() {
      return color("TextArea.inactiveForeground", Color.GRAY);
   }

   private static Color color(String key, Color fallback) {
      Color color = UIManager.getColor(key);
      return color == null ? fallback : color;
   }
}
