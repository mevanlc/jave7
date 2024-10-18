package net.disy.commons.swing.font;

import java.awt.Font;
import net.disy.commons.core.text.font.FontStyle;

public class FontStyleFactory {
   public static FontStyle getFrom(Font font) {
      return FontStyle.getStyle(font.isBold(), font.isItalic());
   }
}
