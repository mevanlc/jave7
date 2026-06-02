package de.jave.preferences;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import net.dizzy.commons.core.util.Ensure;

public class SmartPreferences {
   private final Preferences preferences;

   public SmartPreferences(Preferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
   }

   public final Preferences getSubPreferences(String path) {
      return this.preferences.node(path);
   }

   public final void put(String key, String value) {
      if (value == null) {
         this.preferences.remove(key);
      } else {
         this.preferences.put(key, value);
      }
   }

   protected void put(String key, File file) {
      this.put(key, file == null ? null : file.getAbsolutePath());
   }

   protected final File getFile(String key) {
      String fileName = this.get(key, null);
      return fileName == null ? null : new File(fileName);
   }

   public final void put(String key, int value) {
      this.preferences.putInt(key, value);
   }

   public final void remove(String key) {
      this.preferences.remove(key);
   }

   public final void put(String key, boolean value) {
      this.preferences.putBoolean(key, value);
   }

   public final String get(String key, String defaultValue) {
      return this.preferences.get(key, defaultValue);
   }

   public final int getInt(String key, int defaultValue) {
      return this.preferences.getInt(key, defaultValue);
   }

   public final boolean getBoolean(String key, boolean defaultValue) {
      return this.preferences.getBoolean(key, defaultValue);
   }

   public final void flush() {
      try {
         this.preferences.flush();
      } catch (BackingStoreException var2) {
         var2.printStackTrace();
      }
   }
}
