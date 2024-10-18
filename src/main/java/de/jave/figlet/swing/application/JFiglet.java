package de.jave.figlet.swing.application;

import de.jave.figlet.Figlet;
import de.jave.figlet.util.FigException;
import de.jave.gui.splash.IStartupMonitor;
import de.jave.gui.splash.IStartupRunnable;
import de.jave.gui.splash.SplashScreenManager;
import de.jave.gui.splash.StartupException;
import de.jave.lib.CodeBaseTool;
import java.io.File;
import javax.swing.UIManager;

public class JFiglet {
   private JFiglet() {
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var2) {
      }

      SplashScreenManager splash = new SplashScreenManager(new FigletSplashScreenSetup());
      splash.startup(createStartUpRunnable(args));
   }

   private static IStartupRunnable createStartUpRunnable(final String[] args) {
      return new IStartupRunnable() {
         @Override
         public void startUp(IStartupMonitor monitor) throws StartupException {
            monitor.beginTask("Starting up JFIGlet 0.3...", -1);
            monitor.subTask("Initializing FIGlet font library...");
            File codeBase = CodeBaseTool.getCodeBase(JFiglet.class);
            Figlet figlet = null;
            if (args.length == 0) {
               File fontFolder = null;

               try {
                  fontFolder = new File(codeBase, "fonts");
                  figlet = new Figlet(fontFolder);
               } catch (FigException var7) {
                  if (fontFolder != null) {
                     throw new StartupException(
                        "Unable to load FIGlet fonts from program folder '"
                           + fontFolder.getAbsolutePath()
                           + "'.\n"
                           + "You can either install the FIGlet font library to the program folder or specify\n"
                           + "a font library location as program argument.\n"
                           + "Exiting.",
                        var7
                     );
                  }

                  throw new StartupException(
                     "Unable to determin the FIGlet fonts folder.\nYou can either install the FIGlet font library to the program folder or specify\na font library location as program argument.\nExiting.",
                     var7
                  );
               }
            } else {
               if (args.length != 1) {
                  throw new StartupException("Invalid arguments. Only one optional argument pointing to the\nFIGlet fonts library is supported.");
               }

               try {
                  figlet = new Figlet(args[0]);
               } catch (FigException var6) {
                  throw new StartupException(
                     "Unable to load FIGlet fonts from the specified lirary location '"
                        + args[0]
                        + "'.\n"
                        + "You can either install the FIGlet font library to the program folder or specify\n"
                        + "a valid font library location as program argument.\n"
                        + "Exiting.",
                     var6
                  );
               }
            }

            monitor.subTask("Building application...");
            File tmpFolder = new File(codeBase, "tmp");
            JFigletApplication application = new JFigletApplication(figlet.getFigDriver(), tmpFolder);
            monitor.subTask("Running application...");
            application.start();
         }
      };
   }
}
