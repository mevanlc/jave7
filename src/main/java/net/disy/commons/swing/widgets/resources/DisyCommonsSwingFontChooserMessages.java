package net.disy.commons.swing.fontchooser.resources;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DisyCommonsSwingFontChooserMessages {
   private static final String BUNDLE_NAME = "net.disy.commons.swing.fontchooser.messages";
   private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("net.disy.commons.swing.fontchooser.messages");

   private DisyCommonsSwingFontChooserMessages() {
   }

   public static void reset() {
      RESOURCE_BUNDLE = ResourceBundle.getBundle("net.disy.commons.swing.fontchooser.messages");
   }

   public static String getFormattedString(String key, Object arg) {
      return MessageFormat.format(getString(key), arg);
   }

   public static String getFormattedString(String key, Object[] args) {
      return MessageFormat.format(getString(key), args);
   }

   public static String getString(String key) {
      try {
         return RESOURCE_BUNDLE.getString(key);
      } catch (MissingResourceException var2) {
         return '!' + key + '!';
      }
   }
}
