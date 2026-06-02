package net.dizzy.commons.swing.util;

import javax.swing.SwingUtilities;

public final class EventDispatchThreadUtilities {
   private EventDispatchThreadUtilities() {
   }

   public static void ensureIsEventDispatchThread() {
      if (!SwingUtilities.isEventDispatchThread()) {
         throw new IllegalStateException("Must be called on the event dispatch thread");
      }
   }
}
