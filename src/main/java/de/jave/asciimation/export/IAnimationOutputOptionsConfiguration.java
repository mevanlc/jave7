package de.jave.asciimation.export;

import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.FileExtension;

public interface IAnimationOutputOptionsConfiguration {
   boolean isLoopAvailable();

   boolean isControlsAvailable();

   ExtensionFileFilter[] getOutputFileFilters();

   FileExtension getDefaultOutputFileExtension();
}
