package de.jave.jave.actions;

import de.jave.internet.SwingBrowserLauncher;
import de.jave.jave.version.JaveVersion;
import java.awt.Component;
import net.disy.commons.swing.action.SmartAction;

public class VersionCheckAction extends SmartAction {
   private static final String BASE_URL_STRING = "http://www.jave.de/versioncheck/";

   public VersionCheckAction() {
      super("Check for Updates");
   }

   @Override
   protected void execute(Component parentComponent) {
      String fileName = this.createVersionFileName();
      String urlString = "http://www.jave.de/versioncheck/" + fileName;
      SwingBrowserLauncher.performLaunchBrowser(parentComponent, urlString);
   }

   private String createVersionFileName() {
      String versionNumber = JaveVersion.getFullVersionNumber();
      versionNumber = versionNumber.replace(' ', '_');
      versionNumber = versionNumber.toLowerCase();
      return "jave" + versionNumber + ".html";
   }
}
