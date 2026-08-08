package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class AsciiAntialiasing extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "antialias";
   }

   @Override
   public String getMenuItemLabel() {
      return "Antialias";
   }

   protected static boolean isEmpty(int[][] ch, int x, int y) {
      return ch[y][x] == ' ';
   }

   protected static boolean isSet(int[][] ch, int x, int y) {
      return ch[y][x] != ' '
         && ch[y][x] != '"'
         && ch[y][x] != '`'
         && ch[y][x] != 180
         && ch[y][x] != '\''
         && ch[y][x] != '_'
         && ch[y][x] != '.'
         && ch[y][x] != ',';
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int[][] ch = plate.glyphPlane();
      int height = plate.getHeight();
      int width = plate.getWidth();

      for (int y = 0; y + 1 < height; y++) {
         for (int x = 0; x + 3 < width; x++) {
            if (isSet(ch, x + 1, y)
               && isSet(ch, x + 2, y)
               && isSet(ch, x + 3, y)
               && isSet(ch, x, y + 1)
               && isEmpty(ch, x + 1, y + 1)
               && isEmpty(ch, x + 2, y + 1)
               && isEmpty(ch, x + 3, y + 1)) {
               ch[y + 1][x + 1] = '"';
               ch[y + 1][x + 2] = '\'';
            } else if (isSet(ch, x, y)
               && isSet(ch, x + 1, y)
               && isSet(ch, x + 2, y)
               && isSet(ch, x + 3, y + 1)
               && isEmpty(ch, x, y + 1)
               && isEmpty(ch, x + 1, y + 1)
               && isEmpty(ch, x + 2, y + 1)) {
               ch[y + 1][x + 1] = '`';
               ch[y + 1][x + 2] = '"';
            }
         }
      }

      for (int y = 0; y + 1 < height; y++) {
         for (int xx = 0; xx + 2 < width; xx++) {
            if (isSet(ch, xx + 1, y) && isSet(ch, xx + 2, y) && isSet(ch, xx, y + 1) && isEmpty(ch, xx + 1, y + 1) && isEmpty(ch, xx + 2, y + 1)) {
               ch[y + 1][xx + 1] = '"';
            } else if (isSet(ch, xx, y) && isSet(ch, xx + 1, y) && isSet(ch, xx + 2, y + 1) && isEmpty(ch, xx, y + 1) && isEmpty(ch, xx + 1, y + 1)) {
               ch[y + 1][xx + 1] = '"';
            }
         }
      }

      for (int y = 0; y + 1 < height; y++) {
         for (int xxx = 0; xxx + 1 < width; xxx++) {
            if (isSet(ch, xxx + 1, y) && isSet(ch, xxx, y + 1) && isEmpty(ch, xxx + 1, y + 1)) {
               ch[y + 1][xxx + 1] = '\'';
            }

            if (isSet(ch, xxx + 1, y) && isSet(ch, xxx, y + 1) && isEmpty(ch, xxx, y)) {
               ch[y][xxx] = ',';
            }

            if (isSet(ch, xxx, y) && isSet(ch, xxx + 1, y + 1) && isEmpty(ch, xxx, y + 1)) {
               ch[y + 1][xxx] = '`';
            }

            if (isSet(ch, xxx, y) && isSet(ch, xxx + 1, y + 1) && isEmpty(ch, xxx + 1, y)) {
               ch[y][xxx + 1] = '.';
            }
         }
      }

      return plate;
   }
}
