package de.jave.figlet.engine;

import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.util.FigException;

public interface IFigFontProvider {
   FigFont getFont(String var1) throws FigException;
}
