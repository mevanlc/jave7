package net.disy.commons.swing.util;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import net.disy.commons.core.util.ContractFailedException;

public class EventDispatchThreadUtilities {
   private EventDispatchThreadUtilities() {
   }

   public static void ensureIsEventDispatchThread() {
      if (!SwingUtilities.isEventDispatchThread()) {
         throw new ContractFailedException("Expected to be executed on the event dispatch thread, but was '" + Thread.currentThread().getName() + "'");
      }
   }

   public static void invokeAndWait(Runnable runnable) throws InvocationTargetException {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (InterruptedException var2) {
            throw new RuntimeException(var2);
         }
      } else {
         runnable.run();
      }
   }
}
