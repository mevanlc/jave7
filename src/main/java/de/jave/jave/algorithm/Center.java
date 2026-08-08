package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;
import java.awt.Insets;

public class Center extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "center";
   }

   @Override
   public String getMenuItemLabel() {
      return "Center";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      Insets insets = plate.getEmptyInsets();
      int w = plate.getWidth();
      int h = plate.getHeight();
      int dx = 0;
      if (insets.right >= insets.left) {
         dx = (insets.right - insets.left) / 2;
      } else {
         dx = -((insets.left - insets.right + 1) / 2);
      }

      int dy = 0;
      if (insets.bottom >= insets.top) {
         dy = (insets.bottom - insets.top) / 2;
      } else {
         dy = -((insets.top - insets.bottom + 1) / 2);
      }

      if (dx == 0 && dy == 0) {
         return plate;
      } else {
         int[][] chNew = new int[h][w];

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               if (x - dx >= 0 && x - dx < w && y - dy >= 0 && y - dy < h) {
                  chNew[y][x] = plate.glyphAt(x - dx, y - dy);
               } else {
                  chNew[y][x] = ' ';
               }
            }
         }

         return new CharacterPlate(chNew);
      }
   }
}
