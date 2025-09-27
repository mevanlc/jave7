package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class AlignRight extends JaveAlgorithm {
   private static AlignRight instance;

   private AlignRight() {
   }

   public static synchronized AlignRight getInstance() {
      if (instance == null) {
         instance = new AlignRight();
      }

      return instance;
   }

   @Override
   public String getUndoRedoName() {
      return "align right";
   }

   @Override
   public String getMenuItemLabel() {
      return "Align right";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int width = plate.getWidth();
      int height = plate.getHeight();

      for (int y = 0; y < height; y++) {
         char[] line = plate.getLine(y).toCharArray();
         int right = 0;

         while (right < width && line[width - 1 - right] == ' ') {
            right++;
         }

         if (right < width && right > 0) {
            for (int x = 0; x < width; x++) {
               if (x < right) {
                  plate.setForce(x, y, ' ');
               } else {
                  plate.setForce(x, y, line[x - right]);
               }
            }
         }
      }

      return plate;
   }
}
