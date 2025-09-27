package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;

public class Clear extends JaveAlgorithm {
   @Override
   public String getUndoRedoName() {
      return "clear";
   }

   @Override
   public String getMenuItemLabel() {
      return "Clear";
   }

   @Override
   public CharacterPlate apply(CharacterPlate plate) {
      plate.clear();
      return plate;
   }
}
