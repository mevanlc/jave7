package de.jave.maxosx.internal;

import de.jave.maxosx.IMacOsXApplicationCallbacks;
import java.lang.reflect.Method;
import net.dizzy.commons.core.util.Ensure;

public class MacOsXCallbacksAdapter {
   private final IMacOsXApplicationCallbacks callbacks;

   public MacOsXCallbacksAdapter(IMacOsXApplicationCallbacks callbacks) {
      Ensure.ensureArgumentNotNull(callbacks);
      this.callbacks = callbacks;
   }

   public void about() {
      this.callbacks.performShowAboutDialog();
   }

   public void preferences() {
      this.callbacks.performShowPreferencesDialog();
   }

   public boolean quit() {
      return this.callbacks.performShowExitDialog();
   }

   public Method getQuitMethod() throws SecurityException, NoSuchMethodException {
      return this.getClass().getDeclaredMethod("quit", (Class<?>[])null);
   }

   public Method getAboutMethod() throws SecurityException, NoSuchMethodException {
      return this.getClass().getDeclaredMethod("about", (Class<?>[])null);
   }

   public Method getPreferencesMethod() throws SecurityException, NoSuchMethodException {
      return this.getClass().getDeclaredMethod("preferences", (Class<?>[])null);
   }
}
