package de.jave.figlet.engine.processing;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigLayout;

public class FontSpecifiedFigletJob implements IFigletJob {
   private final FigLayout layout;
   private final FigFont font;
   private final String text;
   private final HorizontalAlignment alignment;

   public FontSpecifiedFigletJob(String text, FigFont font, FigLayout layout, HorizontalAlignment alignment) {
      this.text = text;
      this.font = font;
      this.layout = layout;
      this.alignment = alignment;
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
      return this.alignment;
   }

   @Override
   public FigFont getFont(IFigDriver driver) {
      return this.font;
   }
}
