package de.jave.figlet.engine;

import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.figlet.file.IFigFileLibrary;
import de.jave.figlet.util.FigException;

public interface IFigDriver {
   IFigFileLibrary getFileLibrary();

   FigFont getFont(String var1) throws FigException;

   String figletize(String var1, String var2) throws FigException;

   String figletize(IFigletJob var1) throws FigException;

   String figml(String var1) throws FigException;
}
