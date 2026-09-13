package de.jave.jave.rectangle;

import de.jave.jave.algorithm.rectangle.RectangleStyle;
import net.dizzy.commons.core.exception.UnreachableCodeReachedException;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

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
         case UNICODE_REGULAR:
            return "Unicode - Regular";
         case UNICODE_ROUNDED:
            return "Unicode - Rounded";
         case UNICODE_BOLD:
            return "Unicode - Bold";
         case UNICODE_DOUBLE:
            return "Unicode - Double";
         case UNDERSCORE:
            return "Underscore";
         default:
            throw new UnreachableCodeReachedException();
      }
   }
}
