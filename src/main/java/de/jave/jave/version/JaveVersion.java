package de.jave.jave.version;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JaveVersion {
   private static final String BUNDLE_NAME = "de.jave.jave.version.version";
   private static ResourceBundle resourceBundle;

   private static String getString(String key, String fallback) {
      try {
         return getResourceBundle().getString(key);
      } catch (MissingResourceException var3) {
         return fallback;
      }
   }

   private static ResourceBundle getResourceBundle() {
      if (resourceBundle == null) {
         resourceBundle = ResourceBundle.getBundle("de.jave.jave.version.version");
      }

      return resourceBundle;
   }

   public static String getFullVersionNumber() {
      return getString("Version.version", "");
   }

   public static String getBuildDate() {
      return getString("Version.buildDate", "");
   }
}
