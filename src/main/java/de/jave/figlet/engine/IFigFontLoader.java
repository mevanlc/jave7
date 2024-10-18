package de.jave.figlet.engine;

import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.util.FigException;

public interface IFigFontLoader {
   FigFont loadFont(String var1) throws FigException;

   long getLastModificationTime(String var1) throws FigException;
}
