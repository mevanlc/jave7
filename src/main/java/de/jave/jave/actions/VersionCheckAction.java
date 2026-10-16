package de.jave.jave.actions;

import de.jave.internet.SwingBrowserLauncher;
import java.awt.Component;
import net.dizzy.commons.swing.action.SmartAction;

public class VersionCheckAction extends SmartAction {
   private static final String RELEASES_URL = "https://github.com/mevanlc/jave7/releases";

   public VersionCheckAction() {
      super("Check for Updates");
   }

   @Override
   protected void execute(Component parentComponent) {
      SwingBrowserLauncher.performLaunchBrowser(parentComponent, RELEASES_URL);
   }
}
