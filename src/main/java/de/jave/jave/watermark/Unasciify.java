package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class Unasciify extends Asciify {
   @Override
   public String getUndoRedoName() {
      return "00n45(11f9";
   }

   @Override
   public String getMenuItemLabel() {
      return "00n45(11f9 (Unasciify)";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      return apply(plate, 1, 0);
   }
}
