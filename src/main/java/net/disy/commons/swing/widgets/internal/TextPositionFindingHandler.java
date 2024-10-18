package net.disy.commons.swing.widgets.internal;

import java.awt.FontMetrics;
import java.awt.Point;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.Range;

public final class TextPositionFindingHandler implements IBlockRenderingHandler {
   private final FontMetrics metrics;
   private final Point point;
   private TextPosition textPosition;

   public TextPositionFindingHandler(FontMetrics metrics, Point point) {
      Ensure.ensureArgumentNotNull(metrics);
      Ensure.ensureArgumentNotNull(point);
      this.metrics = metrics;
      this.point = point;
   }

   @Override
   public void handleText(int blockIndex, String text, int x, int lineIndex, int lineHeight, Range optionalSelectionRange) {
      int stringWidth = this.metrics.stringWidth(text);
      if (this.isVerticallyInside(lineIndex, lineHeight)) {
         if (this.isHorizontallyInside(x, x + stringWidth)) {
            int indexInBlock = getIndexInBlock(this.metrics, text, this.point.x - x);
            this.textPosition = new TextPosition(blockIndex, indexInBlock);
         } else if (this.textPosition == null) {
            this.textPosition = new TextPosition(blockIndex, 0);
         }
      }
   }

   private static int getIndexInBlock(FontMetrics metrics, String text, int x) {
      for (int index = 1; index <= text.length(); index++) {
         String substring = text.substring(0, index);
         int width = metrics.stringWidth(substring);
         if (width > x) {
            return index - 1;
         }
      }

      return text.length();
   }

   private boolean isHorizontallyInside(int minX, int maxX) {
      return this.point.x >= minX && this.point.x <= maxX;
   }

   private boolean isVerticallyInside(int lineIndex, int lineHeight) {
      int yOffset = this.metrics.getLeading() + this.metrics.getAscent();
      int yMin = yOffset + (lineIndex - 1) * lineHeight;
      int yMax = yMin + lineHeight;
      return this.point.y <= yMax && this.point.y >= yMin;
   }

   @Override
   public void handleWhiteSpace(int minX, int maxX, int lineIndex, TextPosition whiteSpacePosition, int lineHeight, boolean selected) {
      if (this.isVerticallyInside(lineIndex, lineHeight) && this.isHorizontallyInside(minX, maxX)) {
         this.textPosition = whiteSpacePosition;
      }
   }

   @Override
   public void handleLineEndsAt(int blockIndex, int blockLength, int x, int lineIndex, int height) {
      if (this.isVerticallyInside(lineIndex, height) && this.point.x >= x) {
         this.textPosition = new TextPosition(blockIndex, blockLength);
      }
   }

   public TextPosition getTextPosition() {
      return this.textPosition;
   }
}
