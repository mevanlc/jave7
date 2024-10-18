package de.jave.figlet.swing.action;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.swing.application.FontCategorizer;

public abstract class FontCategorizationAction extends AbstractFontListAction {
   public FontCategorizationAction(IFigDriver figlet) {
      super(figlet, "Categorization...");
   }

   protected void execute(FigFont font) {
      IFigDriver figlet = this.getFiglet();
      new FontCategorizer(figlet).show();
   }
}
