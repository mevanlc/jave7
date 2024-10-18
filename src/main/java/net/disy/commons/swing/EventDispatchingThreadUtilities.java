package net.disy.commons.swing;

import java.lang.reflect.InvocationTargetException;
import net.disy.commons.swing.util.EventDispatchThreadUtilities;

@Deprecated
public final class EventDispatchingThreadUtilities {
   private EventDispatchingThreadUtilities() {
   }

   public static void invokeAndWait(Runnable runnable) throws InvocationTargetException {
      EventDispatchThreadUtilities.invokeAndWait(runnable);
   }
}
