package net.disy.commons.swing.text;

import javax.swing.Icon;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;

public class TextActionResources {
   public static final Icon TEXT_SYMBOL = getImageIcon("textsymbol.gif");
   public static final Icon ALIGN_LEFT = getImageIcon("alignleft.gif");
   public static final Icon ALIGN_CENTER = getImageIcon("aligncenter.gif");
   public static final Icon ALIGN_RIGHT = getImageIcon("alignright.gif");

   private TextActionResources() {
      throw new UnreachableCodeReachedException();
   }

   private static Icon getImageIcon(String relativePath) {
      return DisyCommonsSwingImageProvider.getInstance().getImageIcon("action/text/" + relativePath);
   }
}
