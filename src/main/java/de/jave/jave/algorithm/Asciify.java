package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class Asciify extends JaveAlgorithm {
   protected static final String[][] ASCIIFY_RULES = new String[][]{
      {"u", "00"},
      {"U", "00"},
      {"a", "4"},
      {"A", "4"},
      {"±", "4"},
      {"·", "4"},
      {"b", "8"},
      {"B", "8"},
      {"c", "("},
      {"C", "("},
      {"æ", "("},
      {"Æ", "("},
      {"e", "3"},
      {"E", "3"},
      {"ê", "3"},
      {"Ê", "3"},
      {"i", "1"},
      {"I", "1"},
      {"o", "0"},
      {"O", "0"},
      {"ó", "0"},
      {"Ó", "0"},
      {"s", "5"},
      {"S", "5"},
      {"¶", "5"},
      {"¦", "5"},
      {"t", "7"},
      {"T", "7"},
      {"y", "9"},
      {"Y", "9"},
      {"z", "2"},
      {"Z", "2"},
      {"¿", "2"},
      {"¯", "2"},
      {"¥", "2"},
      {"¬", "2"},
      {"N", "|\\|"},
      {"n", "|\\|"},
      {"M", "|\\/|"},
      {"m", "|\\/|"},
      {"H", "|-|"},
      {"h", "|-|"},
      {"L", "|"},
      {"l", "|"}
   };

   @Override
   public String getUndoRedoName() {
      return "45(11f9";
   }

   @Override
   public String getMenuItemLabel() {
      return "45(11f9 (Asciify)";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      return apply(plate, 0, 1);
   }

   public static final CharacterPlate apply(CharacterPlate plate, int index1, int index2) {
      StringBuffer result = new StringBuffer();
      int width = plate.getWidth();
      int height = plate.getHeight();

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            boolean replaced = false;

            for (int i = 0; i < ASCIIFY_RULES.length; i++) {
               if (plate.equals(ASCIIFY_RULES[i][index1], x, y)) {
                  result.append(ASCIIFY_RULES[i][index2]);
                  replaced = true;
                  x += ASCIIFY_RULES[i][index1].length() - 1;
                  break;
               }
            }

            if (!replaced) {
               result.append(plate.glyphAt(x, y));
            }
         }

         if (y < height - 1) {
            result.append('\n');
         }
      }

      return new CharacterPlate(result.toString());
   }
}
