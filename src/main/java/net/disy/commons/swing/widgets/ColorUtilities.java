package net.disy.commons.swing.color;

import java.awt.Color;

public class ColorUtilities {
   protected ColorUtilities() {
      throw new UnsupportedOperationException();
   }

   public static Color createIntermediateColor(Color color1, Color color2) {
      int blue = (color1.getBlue() + color2.getBlue()) / 2;
      int red = (color1.getRed() + color2.getRed()) / 2;
      int green = (color1.getGreen() + color2.getGreen()) / 2;
      return new Color(red, green, blue);
   }

   public static float getHue(Color color) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
      return hsb[0];
   }

   public static float getSaturation(Color color) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
      return hsb[1];
   }

   public static float getBrightness(Color color) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
      return hsb[2];
   }

   public static Color getTransparentColor(Color color, int alpha) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
   }
}
