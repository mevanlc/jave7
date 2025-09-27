package de.jave.jave.tool.fill;

import de.jave.jave.algorithm.fill.FillMode;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class FillModeUi extends AbstractObjectUi<FillMode> {
   public String getLabel(FillMode value) {
      switch (value) {
         case GRADIENT:
            return "Gradient";
         case SOLID:
            return "Solid";
         case PATTERN:
            return "Pattern";
         default:
            throw new IllegalArgumentException();
      }
   }
}
