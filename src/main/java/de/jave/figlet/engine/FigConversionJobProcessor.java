package de.jave.figlet.engine;

import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.lib.job.IResultConsumer;
import de.jave.lib.job.IWarningCollector;
import net.dizzy.commons.core.asynchronous.IJobProcessor;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.ProgressUtilities;
import net.dizzy.commons.core.util.Ensure;

public class FigConversionJobProcessor implements IJobProcessor<IFigletJob> {
   private final IWarningCollector warner;
   private final IFigDriver figDriver;
   private final IResultConsumer resultConsumer;

   public FigConversionJobProcessor(IFigDriver figDriver, IResultConsumer resultConsumer, IWarningCollector warner) {
      Ensure.ensureArgumentNotNull(warner);
      Ensure.ensureArgumentNotNull(figDriver);
      this.warner = warner;
      this.resultConsumer = resultConsumer;
      this.figDriver = figDriver;
   }

   public void process(ICancelable cancelable, IFigletJob job) throws InterruptedException {
      String t;
      try {
         this.warner.clear();
         t = this.figDriver.figletize(job);
      } catch (Exception var5) {
         ProgressUtilities.checkInterrupted(cancelable);
         if (this.warner != null) {
            this.warner.addWarning("Error with FIGML! " + var5.toString());
         }

         var5.printStackTrace();
         t = var5.toString();
      }

      this.resultConsumer.putResult(t);
   }
}
