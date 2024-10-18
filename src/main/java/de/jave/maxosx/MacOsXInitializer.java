package de.jave.maxosx;

import de.jave.maxosx.internal.MacOsXCallbacksAdapter;
import de.jave.maxosx.internal.MacOsXInterface;
import javax.swing.JMenu;

public class MacOsXInitializer {
   public static void initializeApplicationCallbacks(IMacOsXApplicationCallbacks callbacks) {
      MacOsXCallbacksAdapter adapter = new MacOsXCallbacksAdapter(callbacks);

      try {
         MacOsXInterface.setQuitHandler(adapter, adapter.getQuitMethod());
         MacOsXInterface.setAboutHandler(adapter, adapter.getAboutMethod());
         MacOsXInterface.setPreferencesHandler(adapter, adapter.getPreferencesMethod());
      } catch (NoSuchMethodException var3) {
      }
   }

   public static void setApplicationNameProperty(String applicationName) {
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", applicationName);
   }

   public static void setMnemonic(JMenu menu, char mnemonicChar) {
      if (!isMacOs()) {
         menu.setMnemonic(mnemonicChar);
      }
   }

   public static boolean isMacOs() {
      String os = System.getProperty("os.name").toLowerCase();
      return os.contains("mac");
   }
}
