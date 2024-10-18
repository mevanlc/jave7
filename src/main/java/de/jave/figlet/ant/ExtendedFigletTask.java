package de.jave.figlet.ant;

import de.jave.figlet.Figlet;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.primitives.HorizontalLayoutMode;
import de.jave.figlet.engine.processing.FigletJobFactory;
import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.figlet.util.FigException;

public class ExtendedFigletTask extends FigletTask {
   private static Figlet cachedFigletEngine;
   private boolean monospaceLayout = false;

   @Override
   protected String figletize(Figlet figlet, String rawText) throws FigException {
      if (this.monospaceLayout) {
         FigFont font = figlet.getFigDriver().getFont(this.getFontName());
         FigLayout layout = (FigLayout)font.getLayout().clone();
         layout.setHorizontalMode(HorizontalLayoutMode.FIXED_WIDTH);
         IFigletJob job = FigletJobFactory.createJob(rawText, font, layout);
         return figlet.getFigDriver().figletize(job);
      } else {
         return super.figletize(figlet, rawText);
      }
   }

   public void setMonospaceLayout(boolean monospaceLayout) {
      this.monospaceLayout = monospaceLayout;
   }

   @Override
   protected Figlet createFiglet() throws FigException {
      if (cachedFigletEngine == null) {
         cachedFigletEngine = super.createFiglet();
      }

      return cachedFigletEngine;
   }
}
