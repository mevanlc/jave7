package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class AlignLeft extends JaveAlgorithm {
   private static AlignLeft instance;

   private AlignLeft() {
   }

   public static synchronized AlignLeft getInstance() {
      if (instance == null) {
         instance = new AlignLeft();
      }

      return instance;
   }

   @Override
   public String getUndoRedoName() {
      return "align left";
   }

   @Override
   public String getMenuItemLabel() {
      return "Align left";
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

         if (left < width && left > 0) {
            for (int x = 0; x < width - left; x++) {
               plate.setForce(x, y, plate.get(x + left, y));
            }

            for (int x = width - left; x < width; x++) {
               plate.setForce(x, y, ' ');
            }
         }
      }

      return plate;
   }
}
