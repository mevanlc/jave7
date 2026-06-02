package de.jave.jave.help;

import de.jave.internet.SwingBrowserLauncher;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;

public final class JaveOnlineHelpHandler implements IDialogHelpHandler {
   private static final String BASE_URL = "http://www.jave.de/docs/";
   public static final String FORMULA_EDITOR_DIALOG = "formula2/formula2.html";
   public static final String FIGLET_EXPORT = "figletexport/figletexport.html";
   private final String documentPath;

   public JaveOnlineHelpHandler(String documentPath) {
      Ensure.ensureArgumentNotNull(documentPath);
      this.documentPath = documentPath;
   }

   @Override
   public void execute(Component parentComponent) {
      SwingBrowserLauncher.performLaunchBrowser(parentComponent, "http://www.jave.de/docs/" + this.documentPath);
   }
}
