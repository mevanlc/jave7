package de.jave.jave.menu;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MenuSearchModel {
   public static final int RESULTS_WIDTH = 320;
   public static final int MAX_ROWS = 12;
   private static final Pattern GRAPHEME = Pattern.compile("\\X");

   private MenuSearchModel() {}

   public static List<MenuEntry> match(List<MenuEntry> entries, String query, List<String> history) {
      String normalized = query.strip().toLowerCase(Locale.ROOT);
      if (normalized.isEmpty()) {
         Map<String, MenuEntry> byId = new LinkedHashMap<>();
         entries.forEach(entry -> byId.put(entry.command().id(), entry));
         return history.stream().map(byId::get).filter(java.util.Objects::nonNull).toList();
      }
      String[] words = normalized.split("\\s+");
      return entries.stream().filter(entry -> {
         String label = entry.command().label().toLowerCase(Locale.ROOT);
         return Arrays.stream(words).allMatch(label::contains);
      }).toList();
   }

   public record Page(List<MenuEntry> entries, int omitted) {
      public Page {
         entries = List.copyOf(entries);
      }
   }

   public static Page page(List<MenuEntry> matches, int rowLimit) {
      int limit = Math.max(1, Math.min(MAX_ROWS, rowLimit));
      int shown = matches.size() <= limit ? matches.size() : limit - 1;
      return new Page(matches.subList(0, shown), matches.size() - shown);
   }

   /** -1 is the search field; informational rows never enter the cycle. */
   public static int nextFocus(int current, int direction, int resultCount) {
      return Math.floorMod(current + 1 + direction, resultCount + 1) - 1;
   }

   public static String elide(String text, FontMetrics metrics, int availableWidth) {
      if (metrics.stringWidth(text) <= availableWidth) {
         return text;
      }
      List<Integer> boundaries = new ArrayList<>();
      boundaries.add(0);
      Matcher matcher = GRAPHEME.matcher(text);
      while (matcher.find()) {
         boundaries.add(matcher.end());
      }
      for (int i = boundaries.size() - 2; i >= 0; i--) {
         String candidate = text.substring(0, boundaries.get(i)) + "…";
         if (metrics.stringWidth(candidate) <= availableWidth) {
            return candidate;
         }
      }
      return "…";
   }
}
