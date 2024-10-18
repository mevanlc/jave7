package net.disy.commons.core.os;

import java.io.IOException;

public class WindowsUtilities {
   private static final String SYSTEM_PROPERTY_OS_NAME = "os.name";
   private static final String WINDOWS_NT_COMMAND = "cmd.exe /c ";
   private static final String WINDOWS_9X_COMMAND = "command.com /c ";

   private static boolean isWindowsNt() {
      String os = getOsProperty();
      return os.indexOf("nt") > -1
         || os.indexOf("windows 2000") > -1
         || os.indexOf("windows xp") > -1
         || os.indexOf("windows 2003") > -1
         || os.indexOf("vista") > -1;
   }

   private static String getOsProperty() {
      return System.getProperty("os.name").toLowerCase();
   }

   private static boolean isWindows9x() {
      return getOsProperty().indexOf("windows 9") > -1;
   }

   public static boolean isWindows() {
      return isWindowsNt() || isWindows9x();
   }

   public static Process executeMsdosCommand(String command) throws IOException {
      Runtime runtime = Runtime.getRuntime();
      if (isWindowsNt()) {
         return runtime.exec("cmd.exe /c " + command);
      } else if (isWindows9x()) {
         return runtime.exec("command.com /c " + command);
      } else {
         throw new IllegalStateException("Operating System must be Windows");
      }
   }
}
