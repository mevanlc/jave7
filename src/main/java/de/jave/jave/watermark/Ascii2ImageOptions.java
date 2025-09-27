package de.jave.jave.export;

import java.awt.Color;
import java.awt.Font;
import net.disy.commons.core.util.Ensure;

public class Ascii2ImageOptions {
   private final boolean connectedLinesView;
   private final Font font;
   private final Color foregroundColor;
   private final Color backgroundColor;

   public Ascii2ImageOptions(Font font, boolean connectedLinesView, Color foregroundColor, Color backgroundColor) {
      Ensure.ensureArgumentNotNull(font);
      Ensure.ensureArgumentNotNull(foregroundColor);
      Ensure.ensureArgumentNotNull(backgroundColor);
      this.foregroundColor = foregroundColor;
      this.backgroundColor = backgroundColor;
      this.font = font;
      this.connectedLinesView = connectedLinesView;
   }

   public Font getFont() {
      return this.font;
   }

   public boolean isConnectedLinesView() {
      return this.connectedLinesView;
   }

   public Color getForegroundColor() {
      return this.foregroundColor;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }
}
