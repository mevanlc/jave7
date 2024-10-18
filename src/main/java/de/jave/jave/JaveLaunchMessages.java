package de.jave.jave;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JaveLaunchMessages {
   private static final String BUNDLE_NAME = "de.jave.jave.launchmessages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("de.jave.jave.launchmessages");

   private JaveLaunchMessages() {
   }

   public static String getString(String key) {
      try {
         return RESOURCE_BUNDLE.getString(key);
      } catch (MissingResourceException var2) {
         return '!' + key + '!';
      }
   }

   public static String getString(String key, Object[] values) {
      String string = getString(key);
      return MessageFormat.format(string, values);
   }
}
