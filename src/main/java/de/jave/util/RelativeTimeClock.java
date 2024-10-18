package de.jave.util;

public class RelativeTimeClock {
   protected long lastTimeMillis = System.currentTimeMillis();

   public long getMillisSinceLastCall() {
      long time = System.currentTimeMillis();
      long result = time - this.lastTimeMillis;
      this.lastTimeMillis = time;
      return result;
   }
}
