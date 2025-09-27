package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class CenterByTheLine extends JaveAlgorithm {
   private static CenterByTheLine instance;

   private CenterByTheLine() {
   }

   public static synchronized CenterByTheLine getInstance() {
      if (instance == null) {
         instance = new CenterByTheLine();
      }

      return instance;
   }

   @Override
   public String getUndoRedoName() {
      return "center by the line";
   }

   @Override
   public String getMenuItemLabel() {
      return "Center by the line";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int width = plate.getWidth();
      int height = plate.getHeight();

      for (int y = 0; y < height; y++) {
         char[] line = plate.getLine(y).toCharArray();
         int left = 0;

         while (left < width && line[left] == ' ') {
            left++;
         }

         if (left < width) {
            int right = 0;

            while (right < width && line[width - 1 - right] == ' ') {
               right++;
            }

            int dx = 0;
            if (right >= left) {
               dx = (right - left) / 2;
            } else {
               dx = -((left - right + 1) / 2);
            }

            if (dx != 0) {
               for (int x = 0; x < width; x++) {
                  if (x - dx >= 0 && x - dx < width) {
                     plate.setForce(x, y, line[x - dx]);
                  } else {
                     plate.setForce(x, y, ' ');
                  }
               }
            }
         }
      }

      return plate;
   }
}
