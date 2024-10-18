package de.jave.asciimation;

import de.jave.asciimation.export.IAnimationOutputOptionsConfiguration;
import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.FileExtension;
import net.disy.commons.core.util.Ensure;

public class AnimationOutputOptionsConfiguration implements IAnimationOutputOptionsConfiguration {
   private final boolean controlsAvailable;
   private final boolean loopAvailable;
   private final ExtensionFileFilter[] outputFileFilters;

   public AnimationOutputOptionsConfiguration(ExtensionFileFilter outputFileFilter) {
      this(new ExtensionFileFilter[]{outputFileFilter}, false, false);
   }

   public AnimationOutputOptionsConfiguration(ExtensionFileFilter[] outputFileFilters, boolean loop, boolean control) {
      Ensure.ensureArgumentNotNull(outputFileFilters);
      Ensure.ensureArgumentArrayContentsNotNull(outputFileFilters);
      this.outputFileFilters = outputFileFilters;
      this.loopAvailable = loop;
      this.controlsAvailable = control;
   }

   @Override
   public final boolean isLoopAvailable() {
      return this.loopAvailable;
   }

   @Override
   public final boolean isControlsAvailable() {
      return this.controlsAvailable;
   }

   @Override
   public final ExtensionFileFilter[] getOutputFileFilters() {
      return this.outputFileFilters;
   }

   @Override
   public final FileExtension getDefaultOutputFileExtension() {
      return this.outputFileFilters[0].getExtensions().get(0);
   }
}
