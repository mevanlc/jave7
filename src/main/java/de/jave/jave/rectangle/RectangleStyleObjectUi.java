package de.jave.jave.rectangle;

import de.jave.jave.algorithm.rectangle.RectangleStyle;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class RectangleStyleObjectUi extends AbstractObjectUi<RectangleStyle> {
   public String getLabel(RectangleStyle value) {
      switch (value) {
         case NORMAL:
            return "Normal";
         case CHARACTERS:
            return "Custom";
         case ROUND:
            return "Round";
         case STYLE_1:
            return "Style 1";
         case STYLE_2:
            return "Style 2";
         case STYLE_3:
            return "Style 3";
         case STYLE_4:
            return "Style 4";
         case UNDERSCORE:
            return "Underscore";
         default:
            throw new UnreachableCodeReachedException();
      }
   }
}
