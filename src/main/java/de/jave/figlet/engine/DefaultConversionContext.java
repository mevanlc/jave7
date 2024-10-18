package de.jave.figlet.engine;

import de.jave.lib.job.IWarningCollector;

public class DefaultConversionContext implements IFigConversionContext {
   private final IWarningCollector warner;

   public DefaultConversionContext(IWarningCollector warner) {
      this.warner = warner;
   }

   @Override
   public void addWarning(String message) {
      if (this.warner != null) {
         this.warner.addWarning(message);
      }
   }
}
