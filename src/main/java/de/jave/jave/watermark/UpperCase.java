package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class UpperCase extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "uppercase";
   }

   @Override
   public String getMenuItemLabel() {
      return "Uppercase";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int h = plate.getHeight();
      int w = plate.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            char c = plate.get(x, y);
            if (Character.isLowerCase(c)) {
               plate.setForce(x, y, Character.toUpperCase(c));
            }
         }
      }

      return plate;
   }
}
