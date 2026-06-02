package de.jave.jave.figlet;

import de.jave.figlet.engine.primitives.FigFont;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.ObjectUtilities;

public class FigFontModel extends AbstractChangeableModel {
   private FigFont font;

   public void setFont(FigFont font) {
      if (!ObjectUtilities.equals(this.font, font)) {
         this.font = font;
         this.fireChangeEvent();
      }
   }

   public FigFont getFont() {
      return this.font;
   }
}
