package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.CharacterPlate;

public class Invert extends JaveAlgorithm {
   protected static final String SOURCE = "M! 8_\"d'P,.YL`o[]bF";
   protected static final String DESTINATION = " !8 \"_'d,PY.`b\"][`,";

   @Override
   public String getUndoRedoName() {
      return "invert b/w";
   }

   @Override
   public String getMenuItemLabel() {
      return "Invert b/w";
   }

   @Override
   public JaveSelection apply(JaveSelection sel) {
      int width = sel.getWidth();
      int height = sel.getHeight();
      CharacterPlate plate = sel.getContent();

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            if (sel.isActive(x, y)) {
               int index = "M! 8_\"d'P,.YL`o[]bF".indexOf(plate.get(x, y));
               if (index != -1) {
                  plate.setForce(x, y, " !8 \"_'d,PY.`b\"][`,".charAt(index));
               }
            }
         }
      }

      return sel;
   }
}
