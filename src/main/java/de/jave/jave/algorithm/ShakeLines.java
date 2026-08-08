package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;

public class ShakeLines extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "shake lines";
   }

   @Override
   public String getMenuItemLabel() {
      return "Shake lines";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int[][] ch = plate.glyphPlane();
      int h = plate.getHeight();
      int w = plate.getWidth();
      int[][] chNew = new int[h][w];

      for (int y = 0; y < h; y++) {
         int x0 = 0;

         while (ch[y][x0] == ' ' && x0 < w - 1) {
            x0++;
         }

         int x1 = 0;

         while (ch[y][w - x1 - 1] == ' ' && x1 < w - 1) {
            x1++;
         }

         int r = Toolbox.random(-x0, x1);

         for (int x = 0; x < w; x++) {
            if (x - r < w && x - r >= 0) {
               chNew[y][x] = ch[y][x - r];
            } else {
               chNew[y][x] = ' ';
            }
         }
      }

      return new CharacterPlate(chNew);
   }
}
