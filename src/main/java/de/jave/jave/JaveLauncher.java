package de.jave.jave;

import de.jave.jave.application.startup.JaveMainApplicationStarter;

public class JaveLauncher {
   public static void launchJave(String[] arguments) {
      if (arguments != null && arguments.length > 0) {
         CommandLineJave clj = new CommandLineJave(arguments);
         if (clj.doIt()) {
            System.exit(0);
            return;
         }
      } else {
         JaveMainApplicationStarter.startJaveApplication(arguments);
      }
   }
}
