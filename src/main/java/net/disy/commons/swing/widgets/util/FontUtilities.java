package net.disy.commons.swing.fontchooser.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import javax.swing.UIManager;

public class FontUtilities {
   public static final FontRenderContext DEFAULT_FONT_RENDER_CONTEXT = new FontRenderContext(null, false, false);
   private static final Integer[] STANDARD_SIZES = new Integer[]{
           6,
           7,
           8,
           9,
           10,
           11,
           12,
           13,
           14,
           15,
           16,
           18,
           20,
           22,
           24,
           26,
           28,
           36,
           48,
           72
   };

   private FontUtilities() {
   }

   public static Font getDefaultFont() {
      return UIManager.getFont("TextPane.font");
   }

   public static Integer[] getStandardSizes() {
      return STANDARD_SIZES;
   }

   public static String[] getAvailableFontFamilyNames() {
      return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
   }

   public static boolean isFixedWidth(Font font) {
      return getCharacterWidth(font, '#') == getCharacterWidth(font, '|') && getCharacterWidth(font, ' ') == getCharacterWidth(font, '@');
   }

   private static int getCharacterWidth(Font font, char character) {
      return (int)font.getStringBounds(new char[]{character}, 0, 1, DEFAULT_FONT_RENDER_CONTEXT).getWidth();
   }

   public static boolean isSymbolFont(Font font) {
      return font.canDisplayUpTo("abzABZ190") != -1;
   }

   public static boolean isValidFontSize(int size) {
      return size >= 2 && size <= 200;
   }
}
