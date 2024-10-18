package de.jave.gui.splash;

public class StartupException extends Exception {
   public StartupException(String message, Throwable cause) {
      super(message, cause);
   }

   public StartupException(String message) {
      super(message);
   }
}
