package net.disy.commons.swing.widgets.internal;

import net.disy.commons.core.number.MaxIntegerValueBuilder;
import net.disy.commons.core.util.Range;

public final class LineCountingRenderingHandler implements IBlockRenderingHandler {
   private final MaxIntegerValueBuilder maxLineNumberBuilder = new MaxIntegerValueBuilder(0);

   @Override
   public void handleText(int blockIndex, String text, int x, int lineIndex, int lineHeight, Range optionalSelectionRange) {
      this.maxLineNumberBuilder.add(lineIndex + 1);
   }

   @Override
   public void handleWhiteSpace(int min, int max, int lineIndex, TextPosition textPosition, int lineHeight, boolean selected) {
      this.maxLineNumberBuilder.add(lineIndex + 1);
   }

   @Override
   public void handleLineEndsAt(int blockIndex, int blockLength, int x, int lineIndex, int height) {
   }

   public int getLineCount() {
      return this.maxLineNumberBuilder.getMaximum();
   }
}
