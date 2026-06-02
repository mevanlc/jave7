package de.jave.jave.tool.freehandalgorrithmic;

import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithmicMode;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class FreehandAlgorithmicModeUi extends AbstractObjectUi<FreehandAlgorithmicMode> {
   public String getLabel(FreehandAlgorithmicMode value) {
      if (value == FreehandAlgorithmicMode.LINES_ROUNDED) {
         return "Line";
      } else if (value == FreehandAlgorithmicMode.LINES_MIDDLE) {
         return "Middle";
      } else if (value == FreehandAlgorithmicMode.CHARACTERS) {
         return "Custom";
      } else {
         throw new IllegalArgumentException();
      }
   }
}
