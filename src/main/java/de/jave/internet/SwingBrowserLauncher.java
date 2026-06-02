package de.jave.internet;

import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class SwingBrowserLauncher {
   private SwingBrowserLauncher() {
   }

   public static void performLaunchBrowser(Component parentComponent, URL url) {
      performLaunchBrowser(parentComponent, url.toExternalForm());
   }

   public static void performLaunchBrowser(Component parentComponent, String urlString) {
      URI uri;
      try {
         uri = new URI(urlString);
      } catch (URISyntaxException var5) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("Error opening browser.", var5));
         return;
      }

      try {
         Desktop.getDesktop().browse(uri);
      } catch (IOException var4) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("Error opening browser.", var4));
      }
   }
}
