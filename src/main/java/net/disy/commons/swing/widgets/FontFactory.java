package net.disy.commons.swing.font;

import java.awt.Font;
import net.disy.commons.core.text.font.FontDescription;
import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.core.text.font.FontStyleProperty;

public class FontFactory {
   private static final FontStyle[] FONT_STYLE_MAPPING = new FontStyle[]{FontStyle.PLAIN, FontStyle.BOLD, FontStyle.ITALIC, FontStyle.BOLD_ITALIC};

   public static FontStyle getStyleFromAwt(int awtStyle) {
      return FONT_STYLE_MAPPING[awtStyle];
   }

   public static FontStyle getStyle(Font font) {
      return getStyleFromAwt(font.getStyle());
   }

   public static int getAwtStyle(FontStyle fontStyle) {
      for (int awtStyle = 0; awtStyle < FONT_STYLE_MAPPING.length; awtStyle++) {
         if (FONT_STYLE_MAPPING[awtStyle].equals(fontStyle)) {
            return awtStyle;
         }
      }

      throw new IllegalArgumentException("Unknown fontstyle " + fontStyle);
   }

   public static Font createFont(String fontFamilyName, FontStyle style, int size) {
      return new Font(fontFamilyName, getAwtStyle(style), size);
   }

   public static FontDescription createFontDescription(Font font) {
      return new FontDescription(font.getFamily(), getStyle(font), font.getSize());
   }

   public static Font createFont(FontDescription font) {
      return createFont(font.getFontFamilyName(), font.getFontStyle(), font.getFontSize());
   }

   public static int getAwtStyle(FontStyleProperty fontStyle) {
      return getAwtStyle(FontStyle.PLAIN.derive(fontStyle, true));
   }
}
