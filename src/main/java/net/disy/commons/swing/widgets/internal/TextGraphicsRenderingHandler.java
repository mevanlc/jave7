package net.disy.commons.swing.widgets.internal;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.Range;
import net.disy.commons.swing.color.SwingColors;

public final class TextGraphicsRenderingHandler implements IBlockRenderingHandler {
   private final Graphics graphics;
   private final FontMetrics metrics;
   private final Color foregroundColor;

   public TextGraphicsRenderingHandler(Graphics graphics, Color foregroundColor) {
      Ensure.ensureArgumentNotNull(graphics);
      Ensure.ensureArgumentNotNull(foregroundColor);
      this.graphics = graphics;
      this.foregroundColor = foregroundColor;
      this.metrics = graphics.getFontMetrics();
   }

   @Override
   public void handleText(int blockIndex, String text, int x, int lineIndex, int lineHeight, Range optionalSelectionRange) {
      int yOffset = this.metrics.getLeading() + this.metrics.getAscent();
      int y = yOffset + lineIndex * lineHeight;
      if (optionalSelectionRange != null) {
         String prefix = text.substring(0, optionalSelectionRange.getLowerBound());
         int dStartX = this.metrics.stringWidth(prefix);
         String selectionPart = text.substring(optionalSelectionRange.getLowerBound(), optionalSelectionRange.getUpperBound());
         String endPart = text.substring(optionalSelectionRange.getUpperBound());
         int dEndX = this.metrics.stringWidth(selectionPart);
         this.graphics.setColor(SwingColors.getTextAreaSelectionBackgroundColor());
         this.graphics.fillRect(x + dStartX, lineIndex * lineHeight, dEndX, lineHeight);
         this.graphics.setColor(this.foregroundColor);
         if (prefix.length() > 0) {
            this.graphics.drawString(prefix, x, y);
         }

         if (endPart.length() > 0) {
            this.graphics.drawString(endPart, x + dStartX + dEndX, y);
         }

         this.graphics.setColor(SwingColors.getTextAreaSelectionForegroundColor());
         this.graphics.drawString(selectionPart, x + dStartX, y);
      } else {
         this.graphics.setColor(this.foregroundColor);
         this.graphics.drawString(text, x, y);
      }
   }

   @Override
   public void handleWhiteSpace(int xMin, int xMax, int lineIndex, TextPosition textPosition, int lineHeight, boolean selected) {
      if (selected) {
         this.graphics.setColor(SwingColors.getTextAreaSelectionBackgroundColor());
         this.graphics.fillRect(xMin, lineIndex * lineHeight, xMax - xMin, lineHeight);
      }
   }

   @Override
   public void handleLineEndsAt(int blockIndex, int blockLength, int x, int lineIndex, int height) {
   }
}
