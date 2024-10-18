package net.disy.commons.core.logging;

public interface ILogger {
   boolean isDebugEnabled();

   void debug(String var1);

   void debug(Throwable var1);

   void debug(String var1, Throwable var2);

   void info(String var1);

   void info(Throwable var1);

   void info(String var1, Throwable var2);

   void warn(String var1);

   void warn(Throwable var1);

   void warn(String var1, Throwable var2);

   void error(String var1);

   void error(Throwable var1);

   void error(String var1, Throwable var2);

   @Deprecated
   void fatal(String var1);

   @Deprecated
   void fatal(Throwable var1);

   @Deprecated
   void fatal(String var1, Throwable var2);
}
