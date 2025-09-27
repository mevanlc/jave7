package de.jave.jave.figlet;

import de.jave.figlet.engine.primitives.FigFont;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.ObjectUtilities;

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
