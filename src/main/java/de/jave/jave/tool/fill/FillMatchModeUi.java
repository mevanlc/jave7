package de.jave.jave.tool.fill;

import de.jave.jave.algorithm.fill.FillMatchMode;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class FillMatchModeUi extends AbstractObjectUi<FillMatchMode> {
   public String getLabel(FillMatchMode value) {
      if (value == FillMatchMode.EQUAL_CHARACTER) {
         return "Equal char";
      } else if (value == FillMatchMode.ANY_CHARACTER) {
         return "Any char";
      } else if (value == FillMatchMode.ANY_CHARACTER_DIAGONAL) {
         return "Any+diagonal";
      } else {
         throw new IllegalArgumentException();
      }
   }
}
