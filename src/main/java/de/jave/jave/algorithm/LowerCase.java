package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class LowerCase extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "lowercase";
   }

   @Override
   public String getMenuItemLabel() {
      return "Lowercase";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int h = plate.getHeight();
      int w = plate.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int c = plate.glyphAt(x, y);
            if (Character.isUpperCase(c)) {
               plate.setForce(x, y, Character.toLowerCase(c));
            }
         }
      }

      return plate;
   }
}
