package de.jave.jave.actions;

import de.jave.internet.SwingBrowserLauncher;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public class OpenOnlineLocationAction extends SmartAction {
   private final String url;

   public OpenOnlineLocationAction(String name, String url) {
      super(name);
      Ensure.ensureArgumentNotNull(url);
      this.url = url;
   }

   @Override
   protected void execute(Component parentComponent) {
      SwingBrowserLauncher.performLaunchBrowser(parentComponent, this.url);
   }
}
