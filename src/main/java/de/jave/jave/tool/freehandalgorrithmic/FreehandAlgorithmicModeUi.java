package de.jave.jave.tool.freehandalgorrithmic;

import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithmicMode;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class FreehandAlgorithmicModeUi extends AbstractObjectUi<FreehandAlgorithmicMode> {
   public String getLabel(FreehandAlgorithmicMode value) {
      if (value == FreehandAlgorithmicMode.LINES_ROUNDED) {
         return "Line";
      } else if (value == FreehandAlgorithmicMode.LINES_MIDDLE) {
         return "Middle";
      } else if (value == FreehandAlgorithmicMode.CHARACTERS) {
         return "Characters";
      } else {
         throw new IllegalArgumentException();
      }
   }
}
