package de.jave.figlet.engine.processing;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.util.FigException;

public interface IFigletJob {
   String getText();

   FigLayout getLayout();

   HorizontalAlignment getHorizontalAlignment();

   FigFont getFont(IFigDriver var1) throws FigException;
}
