package de.jave.figlet.engine.processing;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.util.FigException;

public class FontnameSpecifiedFigletJob implements IFigletJob {
   private final String fontName;
   private final String text;
   private final FigLayout layout;
   private final HorizontalAlignment horizontalAlignment;

   public FontnameSpecifiedFigletJob(String text, String fontName, FigLayout layout, HorizontalAlignment horizontalAlignment) {
      this.layout = layout;
      this.text = text;
      this.fontName = fontName;
      this.horizontalAlignment = horizontalAlignment;
   }

   @Override
   public String getText() {
      return this.text;
   }

   @Override
   public FigLayout getLayout() {
      return this.layout;
   }

   @Override
   public HorizontalAlignment getHorizontalAlignment() {
      return this.horizontalAlignment;
   }

   @Override
   public FigFont getFont(IFigDriver driver) throws FigException {
      return driver.getFont(this.fontName);
   }
}
