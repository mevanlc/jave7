package net.disy.commons.swing.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DisyCommonsSwingMessages {
   private static final String BUNDLE_NAME = "net.disy.commons.swing.resources.messages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("net.disy.commons.swing.resources.messages");

   private DisyCommonsSwingMessages() {
   }

   public static String getString(String key) {
      try {
         return RESOURCE_BUNDLE.getString(key);
      } catch (MissingResourceException var2) {
         return '!' + key + '!';
      }
   }
}
