package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class Rot13 extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "rot 13";
   }

   @Override
   public String getMenuItemLabel() {
      return "Rot 13";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      int h = plate.getHeight();
      int w = plate.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            char c = plate.get(x, y);
            if ((c < 'a' || c > 'm') && (c < 'A' || c > 'M')) {
               if (c >= 'n' && c <= 'z' || c >= 'N' && c <= 'Z') {
                  plate.setForce(x, y, (char)(c - '\r'));
               }
            } else {
               plate.setForce(x, y, (char)(c + '\r'));
            }
         }
      }

      return plate;
   }
}
