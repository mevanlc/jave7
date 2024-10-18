package net.disy.commons.core.thread;

import net.disy.commons.core.logging.ILogger;
import net.disy.commons.core.util.Ensure;

public class Condition {
   private final ILogger logger;
   private boolean value;

   public Condition(ILogger logger, boolean value) {
      Ensure.ensureArgumentNotNull(logger);
      this.logger = logger;
      this.value = value;
   }

   public synchronized boolean isTrue() {
      return this.value;
   }

   public synchronized void setFalse() {
      this.logger.debug("setting condition to false");
      this.value = false;
   }

   public synchronized void setTrue() {
      this.logger.debug("setting condition to true");
      this.value = true;
      this.notifyAll();
   }

   public synchronized void releaseAll() {
      this.notifyAll();
      this.logger.debug("released all");
   }

   public synchronized void releaseOne() {
      this.notify();
      this.logger.debug("released one");
   }

   public synchronized void waitForTrue(long timeout) throws InterruptedException {
      if (!this.value) {
         this.logger.debug("waiting to become true");
         this.wait(timeout);
      }

      if (this.value) {
         this.logger.debug("now true");
      } else {
         this.logger.warn("timed out, condition is still false!");
      }
   }

   public synchronized void waitForTrue() throws InterruptedException {
      this.waitForTrue(0L);
   }
}
