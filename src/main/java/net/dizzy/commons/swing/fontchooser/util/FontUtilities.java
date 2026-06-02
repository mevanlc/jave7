package net.dizzy.commons.swing.fontchooser.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;

public final class FontUtilities {
   public static final FontRenderContext DEFAULT_FONT_RENDER_CONTEXT = new FontRenderContext(null, false, false);

   private FontUtilities() {
   }

   public static Font[] getAllFonts() {
      return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
   }

   public static boolean isFixedWidth(Font font) {
      return font.canDisplay('i') && font.canDisplay('W') && font.getStringBounds("i", new java.awt.font.FontRenderContext(null, false, false)).getWidth() == font.getStringBounds("W", new java.awt.font.FontRenderContext(null, false, false)).getWidth();
   }
}
