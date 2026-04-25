package de.jave.ascii.plate;

import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JOptionPane;

public class CharacterMetrics {
   private static volatile CellScalingMode currentMode = CellScalingMode.LINE;
   private static volatile float widthScale = 1.0F;
   private static volatile float heightScale = 1.0F;

   private final int width;
   private final int height;
   private final int ascent;

   public CharacterMetrics(int width, int height, int ascent) {
      this.width = width;
      this.height = height;
      this.ascent = ascent;
   }

   public int getAscent() {
      return this.ascent;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public static void setMode(CellScalingMode mode) {
      currentMode = (mode == null) ? CellScalingMode.LINE : mode;
   }

   public static CellScalingMode getMode() {
      return currentMode;
   }

   public static void setScale(float widthScale, float heightScale) {
      CharacterMetrics.widthScale = widthScale;
      CharacterMetrics.heightScale = heightScale;
   }

   public static float getWidthScale() {
      return widthScale;
   }

   public static float getHeightScale() {
      return heightScale;
   }

   public static CharacterMetrics createCharacterMetrics(Font font) {
      FontMetrics fontMetrics = JOptionPane.getRootFrame().getFontMetrics(font);
      CellScalingMode mode = currentMode;
      int charWidth = Math.max(1, mode.width(font, fontMetrics));
      int charHeight = Math.max(1, mode.height(font, fontMetrics));
      int charAscent = mode.ascent(font, fontMetrics);
      return new CharacterMetrics(charWidth, charHeight, charAscent);
   }
}
