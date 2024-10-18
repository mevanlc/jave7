package net.disy.commons.swing.widgets.internal;

import java.util.ArrayList;
import java.util.List;

public class TextContent {
   private final List<TextBlock> textBlocks = new ArrayList<>();

   public void setTextBlocks(List<TextBlock> textBlocks) {
      this.textBlocks.clear();
      this.textBlocks.addAll(textBlocks);
   }

   public TextPosition getLastTextPosition() {
      if (this.isEmpty()) {
         return null;
      } else {
         int lastBlockIndex = this.textBlocks.size() - 1;
         return new TextPosition(lastBlockIndex, this.textBlocks.get(lastBlockIndex).text.length());
      }
   }

   public int getBlockCount() {
      return this.textBlocks.size();
   }

   public TextBlock getBlock(int blockIndex) {
      return this.textBlocks.get(blockIndex);
   }

   public boolean isEmpty() {
      return this.textBlocks.isEmpty();
   }

   public String getText(TextPosition start, TextPosition end) {
      StringBuilder builder = new StringBuilder();

      for (int blockIndex = start.getBlockIndex(); blockIndex <= end.getBlockIndex(); blockIndex++) {
         TextBlock block = this.getBlock(blockIndex);
         if (blockIndex == start.getBlockIndex() && blockIndex == end.getBlockIndex()) {
            builder.append(block.text.subSequence(start.getIndexInBlock(), end.getIndexInBlock()));
         } else if (blockIndex == start.getBlockIndex()) {
            builder.append(block.text.substring(start.getIndexInBlock()));
            appendDelimiter(builder, block);
         } else if (blockIndex == end.getBlockIndex()) {
            builder.append(block.text.subSequence(0, end.getIndexInBlock()));
         } else {
            builder.append(block.text);
            appendDelimiter(builder, block);
         }
      }

      return builder.toString();
   }

   private static void appendDelimiter(StringBuilder builder, TextBlock block) {
      TextBlockDelimiter delimiter = block.delimiter;
      switch (delimiter) {
         case END_OF_TEXT:
         default:
            break;
         case NEWLINE:
            builder.append('\n');
            break;
         case TAB:
            builder.append('\t');
            break;
         case SPACE:
            builder.append(' ');
      }
   }
}
