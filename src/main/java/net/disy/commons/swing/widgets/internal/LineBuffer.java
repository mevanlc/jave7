package net.disy.commons.swing.widgets.internal;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.text.TextAlignment;
import net.disy.commons.core.util.Range;

public class LineBuffer {
   private final List<LineBuffer.LinePart> buffer = new ArrayList<>();
   private final FontMetrics metrics;
   private final int layoutWidth;
   private final IBlockRenderingHandler blockRenderer;
   private final TextAlignment textAlignment;
   private int blockIndexOffset = 0;
   private int lineIndex = 0;
   private final ObjectModel<TextSelection> selectionModel;
   private final int spaceWidth;
   private final int tabWidth;

   public LineBuffer(
      FontMetrics metrics, int layoutWidth, IBlockRenderingHandler blockRenderer, TextAlignment textAlignment, ObjectModel<TextSelection> selectionModel
   ) {
      this.metrics = metrics;
      this.layoutWidth = layoutWidth;
      this.blockRenderer = blockRenderer;
      this.textAlignment = textAlignment;
      this.selectionModel = selectionModel;
      this.spaceWidth = metrics.stringWidth(" ");
      this.tabWidth = metrics.stringWidth("        ");
   }

   public void add(TextBlock block, int blockWidth) {
      this.buffer.add(new LineBuffer.LinePart(block, blockWidth));
   }

   public void handleNewLine() {
      this.renderLine(false);
   }

   public void handleAutoLineBreak() {
      this.renderLine(true);
   }

   private void renderLine(boolean isAutoBreak) {
      int lineWidth = this.calculateLineWidth();
      int spaceLeft = this.layoutWidth - lineWidth;
      int xOffset;
      switch (this.textAlignment) {
         case CENTER:
            xOffset = spaceLeft / 2;
            break;
         case RIGHT:
            xOffset = spaceLeft;
            break;
         default:
            xOffset = 0;
      }

      int x = xOffset;

      for (int blockIndex = 0; blockIndex < this.buffer.size(); blockIndex++) {
         TextBlock block = this.buffer.get(blockIndex).block;
         int blockWidth = this.metrics.stringWidth(block.text);
         int absoluteBlockIndex = blockIndex + this.blockIndexOffset;
         Range selectionRange = this.getSelectionRangeIfAny(this.selectionModel.getValue(), absoluteBlockIndex, block);
         this.blockRenderer.handleText(absoluteBlockIndex, block.text, x, this.lineIndex, this.metrics.getHeight(), selectionRange);
         x += blockWidth;
         TextBlockDelimiter delimiter = block.delimiter;
         boolean delimiterSelected = this.isDelimiterSelected(this.selectionModel.getValue(), block.text.length(), absoluteBlockIndex);
         switch (delimiter) {
            case END_OF_TEXT:
               this.blockRenderer.handleLineEndsAt(absoluteBlockIndex, block.text.length(), x, this.lineIndex, this.metrics.getHeight());
               break;
            case NEWLINE:
               this.blockRenderer.handleLineEndsAt(absoluteBlockIndex, block.text.length(), x, this.lineIndex, this.metrics.getHeight());
               break;
            case SPACE:
               this.blockRenderer
                  .handleWhiteSpace(
                     x,
                     x + this.spaceWidth,
                     this.lineIndex,
                     new TextPosition(absoluteBlockIndex, block.text.length()),
                     this.metrics.getHeight(),
                     delimiterSelected
                  );
               x += this.spaceWidth;
               break;
            case TAB:
               this.blockRenderer
                  .handleWhiteSpace(
                     x,
                     x + this.tabWidth,
                     this.lineIndex,
                     new TextPosition(absoluteBlockIndex, block.text.length()),
                     this.metrics.getHeight(),
                     delimiterSelected
                  );
               x += this.tabWidth;
         }
      }

      this.blockIndexOffset = this.blockIndexOffset + this.buffer.size();
      if (isAutoBreak) {
         this.blockRenderer
            .handleLineEndsAt(
               this.blockIndexOffset - 1, this.buffer.get(this.buffer.size() - 1).block.text.length(), x, this.lineIndex, this.metrics.getHeight()
            );
      }

      this.lineIndex++;
      this.buffer.clear();
   }

   private int calculateLineWidth() {
      int lineWidth = 0;
      int lastDelimiterWidth = 0;

      for (LineBuffer.LinePart linePart : this.buffer) {
         lineWidth += linePart.width;
         switch (linePart.block.delimiter) {
            case SPACE:
               lineWidth += this.spaceWidth;
               lastDelimiterWidth = this.spaceWidth;
               break;
            case TAB:
               lineWidth += this.tabWidth;
               lastDelimiterWidth = this.tabWidth;
               break;
            default:
               lastDelimiterWidth = 0;
         }
      }

      return lineWidth - lastDelimiterWidth;
   }

   private boolean isDelimiterSelected(TextSelection selection, int blockSize, int blockIndex) {
      if (selection == null) {
         return false;
      } else if (selection.startPosition.getBlockIndex() > blockIndex) {
         return false;
      } else if (selection.endPosition.getBlockIndex() < blockIndex) {
         return false;
      } else if (selection.endPosition.getBlockIndex() > blockIndex) {
         return true;
      } else {
         int indexInBlock = selection.endPosition.getIndexInBlock();
         return indexInBlock >= blockSize;
      }
   }

   private Range getSelectionRangeIfAny(TextSelection selection, int blockIndex, TextBlock block) {
      if (selection == null) {
         return null;
      } else if (selection.startPosition.getBlockIndex() > blockIndex) {
         return null;
      } else if (selection.endPosition.getBlockIndex() < blockIndex) {
         return null;
      } else if (selection.startPosition.getBlockIndex() < blockIndex) {
         return selection.endPosition.getBlockIndex() > blockIndex ? new Range(0, block.text.length()) : new Range(0, selection.endPosition.getIndexInBlock());
      } else {
         return selection.endPosition.getBlockIndex() > blockIndex
            ? new Range(selection.startPosition.getIndexInBlock(), block.text.length())
            : new Range(selection.startPosition.getIndexInBlock(), selection.endPosition.getIndexInBlock());
      }
   }

   private class LinePart {
      final TextBlock block;
      final int width;

      public LinePart(TextBlock block, int width) {
         this.block = block;
         this.width = width;
      }
   }
}
