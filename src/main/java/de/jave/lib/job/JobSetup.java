package de.jave.lib.job;

import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.util.Ensure;

public class JobSetup {
   private final ICancelable cancelable;
   private final Object jobObject;

   public JobSetup(Object jobObject, ICancelable cancelable) {
      Ensure.ensureArgumentNotNull(jobObject);
      Ensure.ensureArgumentNotNull(cancelable);
      this.jobObject = jobObject;
      this.cancelable = cancelable;
   }

   public ICancelable getCancelable() {
      return this.cancelable;
   }

   public Object getJobObject() {
      return this.jobObject;
   }
}
