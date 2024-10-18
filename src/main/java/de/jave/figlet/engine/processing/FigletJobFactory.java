package de.jave.figlet.engine.processing;

import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigLayout;

public class FigletJobFactory {
   public static IFigletJob createJob(String text, FigFont font) {
      return new FontSpecifiedFigletJob(text, font, font.getLayout(), HorizontalAlignment.LEFT);
   }

   public static IFigletJob createJob(String text, FigFont font, FigLayout layout) {
      return new FontSpecifiedFigletJob(text, font, layout, HorizontalAlignment.LEFT);
   }

   public static IFigletJob createJob(String text, FigFont font, FigLayout layout, HorizontalAlignment alignment) {
      return new FontSpecifiedFigletJob(text, font, layout, alignment);
   }

   public static IFigletJob createJob(String text, String fontName, FigLayout layout, HorizontalAlignment horizontalAlignment) {
      return new FontnameSpecifiedFigletJob(text, fontName, layout, horizontalAlignment);
   }

   public static IFigletJob createJob(String text, String fontName) {
      return new FontnameSpecifiedFigletJob(text, fontName, null, HorizontalAlignment.LEFT);
   }
}
