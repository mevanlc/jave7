package de.jave.lib.cell;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class GraphemeSplitter {
   private GraphemeSplitter() {
   }

   public static List<String> split(String text) {
      BreakIterator iterator = BreakIterator.getCharacterInstance(Locale.ROOT);
      iterator.setText(text);

      List<String> result = new ArrayList<>();
      int start = iterator.first();
      for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
         result.add(text.substring(start, end));
      }
      return result;
   }
}
