package de.jave.jave.configuration;

import java.io.File;

public class ConfigurationException extends Exception {
   public ConfigurationException(String message) {
      super(message);
   }

   public ConfigurationException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConfigurationException(File file, String message) {
      this("Error in configuration file or folder '" + file.getAbsolutePath() + "': " + message);
   }

   public ConfigurationException(File file, String message, Throwable cause) {
      this("Error in configuration file or folder '" + file.getAbsolutePath() + "': " + message, cause);
   }

   public ConfigurationException(File file, Throwable cause) {
      this("Error in configuration file or folder '" + file.getAbsolutePath() + "'", cause);
   }
}
