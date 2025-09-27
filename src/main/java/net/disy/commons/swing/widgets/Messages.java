package net.disy.commons.swing.filechooser;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {
   private static final String BUNDLE_NAME = "net.disy.commons.swing.filechooser.messages";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("net.disy.commons.swing.filechooser.messages");

   public static String getString(String key) {
      return RESOURCE_BUNDLE.getString(key);
   }

   public static String getString(String key, Object[] arguments) {
      String value = getString(key);
      return value == null ? null : MessageFormat.format(value, arguments);
   }
}
