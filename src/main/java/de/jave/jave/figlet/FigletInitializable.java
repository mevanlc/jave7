package de.jave.jave.figlet;

import de.jave.figlet.engine.FigDriver;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.file.BaseFolderFigFileResource;
import de.jave.figlet.util.FigException;
import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.File;

public class FigletInitializable implements IJaveInitializable<IFigDriver> {
   public IFigDriver initialize(IJavaInitializationContext context) throws ConfigurationException {
      File folder = context.getConfigurationFile("fonts");

      try {
         return new FigDriver(new BaseFolderFigFileResource(folder));
      } catch (FigException var4) {
         throw new ConfigurationException(folder, "Unable to initialize FIGlet", var4);
      }
   }
}
