package de.jave.jave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Organizes the Unicode Box Drawing block by the part each character plays in
 * a drawing.  The common characters are presented in diagrams; uncommon
 * weight transitions are variants of the same structural part.
 */
final class BoxDrawingPalette {
   static final int FIRST_CODE_POINT = 0x2500;
   static final int LAST_CODE_POINT = 0x257f;

   enum Piece {
      HORIZONTAL,
      VERTICAL,
      DOWN_RIGHT,
      DOWN_LEFT,
      UP_RIGHT,
      UP_LEFT,
      VERTICAL_RIGHT,
      VERTICAL_LEFT,
      DOWN_HORIZONTAL,
      UP_HORIZONTAL,
      CROSS,
      DIAGONAL
   }

   static final class Diagram {
      private final String title;
      private final String[] rows;

      Diagram(String title, String... rows) {
         this.title = title;
         this.rows = rows.clone();
      }

      String getTitle() {
         return this.title;
      }

      String[] getRows() {
         return this.rows.clone();
      }
   }

   private static final Map<Piece, String> VARIANTS = createVariants();
   private static final List<Diagram> DIAGRAMS = createDiagrams();
   private static final Set<Character> VISIBLE_CHARACTERS = createVisibleCharacters();

   private BoxDrawingPalette() {}

   static List<Diagram> getDiagrams() {
      return DIAGRAMS;
   }

   static String getVariants(Piece piece) {
      String variants = VARIANTS.get(piece);
      return variants == null ? "" : variants;
   }

   static String getAlternates(char ch) {
      Piece piece = getPiece(ch);
      String variants = getVariants(piece);
      StringBuilder alternates = new StringBuilder();
      for (int i = 0; i < variants.length(); i++) {
         char variant = variants.charAt(i);
         if (!VISIBLE_CHARACTERS.contains(variant)) {
            alternates.append(variant);
         }
      }
      return alternates.toString();
   }

   static Piece getPiece(char ch) {
      for (Map.Entry<Piece, String> entry : VARIANTS.entrySet()) {
         if (entry.getValue().indexOf(ch) >= 0) {
            return entry.getKey();
         }
      }
      return null;
   }

   static Set<Character> getAllCharacters() {
      Set<Character> characters = new LinkedHashSet<>();
      for (String variants : VARIANTS.values()) {
         for (int i = 0; i < variants.length(); i++) {
            characters.add(variants.charAt(i));
         }
      }
      return Collections.unmodifiableSet(characters);
   }

   static Set<Character> getVisibleCharacters() {
      return VISIBLE_CHARACTERS;
   }

   static String getDisplayName(char ch) {
      String name = Character.getName(ch);
      if (name == null) {
         return "Unknown character";
      }
      String prefix = "BOX DRAWINGS ";
      return name.startsWith(prefix) ? name.substring(prefix.length()) : name;
   }

   private static Map<Piece, String> createVariants() {
      Map<Piece, String> variants = new EnumMap<>(Piece.class);
      variants.put(Piece.HORIZONTAL, "─━┄┅┈┉╌╍═╴╶╸╺╼╾");
      variants.put(Piece.VERTICAL, "│┃┆┇┊┋╎╏║╵╷╹╻╽╿");
      variants.put(Piece.DOWN_RIGHT, "┌┍┎┏╒╓╔╭");
      variants.put(Piece.DOWN_LEFT, "┐┑┒┓╕╖╗╮");
      variants.put(Piece.UP_RIGHT, "└┕┖┗╘╙╚╰");
      variants.put(Piece.UP_LEFT, "┘┙┚┛╛╜╝╯");
      variants.put(Piece.VERTICAL_RIGHT, "├┝┞┟┠┡┢┣╞╟╠");
      variants.put(Piece.VERTICAL_LEFT, "┤┥┦┧┨┩┪┫╡╢╣");
      variants.put(Piece.DOWN_HORIZONTAL, "┬┭┮┯┰┱┲┳╤╥╦");
      variants.put(Piece.UP_HORIZONTAL, "┴┵┶┷┸┹┺┻╧╨╩");
      variants.put(Piece.CROSS, "┼┽┾┿╀╁╂╃╄╅╆╇╈╉╊╋╪╫╬");
      variants.put(Piece.DIAGONAL, "╱╲╳");
      return Collections.unmodifiableMap(variants);
   }

   private static List<Diagram> createDiagrams() {
      List<Diagram> diagrams = new ArrayList<>();
      diagrams.add(new Diagram(
         "Light",
         "┌─┬─┐",
         "│ │ │",
         "├─┼─┤",
         "│ │ │",
         "└─┴─┘"
      ));
      diagrams.add(new Diagram(
         "Heavy",
         "┏━┳━┓",
         "┃ ┃ ┃",
         "┣━╋━┫",
         "┃ ┃ ┃",
         "┗━┻━┛"
      ));
      diagrams.add(new Diagram(
         "Double",
         "╔═╦═╗",
         "║ ║ ║",
         "╠═╬═╣",
         "║ ║ ║",
         "╚═╩═╝"
      ));
      diagrams.add(new Diagram(
         "Rounded",
         "╭─╮",
         "│ │",
         "╰─╯",
         "   ",
         "   "
      ));
      diagrams.add(new Diagram(
         "Light dashes",
         "┈┈┈",
         "┄┄┄",
         "╌╌╌",
         "┊┆╎",
         "┊┆╎"
      ));
      diagrams.add(new Diagram(
         "Heavy dashes",
         "┉┉┉",
         "┅┅┅",
         "╍╍╍",
         "┋┇╏",
         "┋┇╏"
      ));
      diagrams.add(new Diagram(
         "Diagonals",
         "╱ ╲",
         " ╳ ",
         "╲ ╱",
         "   ",
         "   "
      ));
      return Collections.unmodifiableList(diagrams);
   }

   private static Set<Character> createVisibleCharacters() {
      Set<Character> characters = new LinkedHashSet<>();
      for (Diagram diagram : DIAGRAMS) {
         for (String row : diagram.getRows()) {
            for (int i = 0; i < row.length(); i++) {
               char ch = row.charAt(i);
               if (ch != ' ') {
                  characters.add(ch);
               }
            }
         }
      }
      return Collections.unmodifiableSet(characters);
   }
}
