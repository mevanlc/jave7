package net.disy.commons.swing.widgets.internal;

import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.util.Ensure;

public class TextBlockFactory {
   public static List<TextBlock> createTextBlocks(String text) {
      Ensure.ensureArgumentNotNull(text);
      int endIndex = 0;
      int startIndex = 0;
      ArrayList<TextBlock> blocks = new ArrayList<>();

      while (endIndex < text.length()) {
         TextBlockDelimiter delimiter = getCorrespondingDelimiterIfAny(text.charAt(endIndex));
         if (delimiter != null) {
            blocks.add(new TextBlock(text.substring(startIndex, endIndex), delimiter));
            startIndex = ++endIndex;
         } else {
            endIndex++;
         }
      }

      if (endIndex > startIndex) {
         blocks.add(new TextBlock(text.substring(startIndex, endIndex), TextBlockDelimiter.END_OF_TEXT));
      }

      return blocks;
   }

   private static TextBlockDelimiter getCorrespondingDelimiterIfAny(char character) {
      if (character == ' ') {
         return TextBlockDelimiter.SPACE;
      } else if (character == '\t') {
         return TextBlockDelimiter.TAB;
      } else {
         return character == '\n' ? TextBlockDelimiter.NEWLINE : null;
      }
   }
}
