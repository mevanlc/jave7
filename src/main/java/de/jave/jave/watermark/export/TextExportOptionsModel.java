package de.jave.jave.actions.export;

import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.preferences.ColorScheme;
import java.awt.Color;
import java.awt.Font;
import net.disy.commons.core.model.AbstractChangeableModel;

public class TextExportOptionsModel extends AbstractChangeableModel implements ITextExportOptions {
   private boolean trim = true;
   private Color foregroundColor = ColorScheme.BLACK_ON_WHITE.getColorText();
   private Color backgroundColor = ColorScheme.BLACK_ON_WHITE.getColorPlateBackground();
   private boolean connectedLinesView = false;
   private Font font = JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH;

   public void setTrim(boolean trim) {
      if (this.trim != trim) {
         this.trim = trim;
         this.fireChangeEvent();
      }
   }

   @Override
   public boolean isTrim() {
      return this.trim;
   }

   public void setForegroundColor(Color foregroundColor) {
      if (!this.foregroundColor.equals(foregroundColor)) {
         this.foregroundColor = foregroundColor;
         this.fireChangeEvent();
      }
   }

   @Override
   public Color getForegroundColor() {
      return this.foregroundColor;
   }

   public void setBackgroundColor(Color backgroundColor) {
      if (!this.backgroundColor.equals(backgroundColor)) {
         this.backgroundColor = backgroundColor;
         this.fireChangeEvent();
      }
   }

   @Override
   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   @Override
   public boolean isConnectedLinesView() {
      return this.connectedLinesView;
   }

   public void setConnectedLinesView(boolean connectedLinesView) {
      if (this.connectedLinesView != connectedLinesView) {
         this.connectedLinesView = connectedLinesView;
         this.fireChangeEvent();
      }
   }

   @Override
   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      if (!this.font.equals(font)) {
         this.font = font;
         this.fireChangeEvent();
      }
   }
}
