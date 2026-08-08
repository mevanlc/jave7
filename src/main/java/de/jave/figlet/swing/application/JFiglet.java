package de.jave.figlet.swing.application;

import de.jave.figlet.Figlet;
import de.jave.figlet.util.FigException;
import de.jave.lib.CodeBaseTool;
import java.io.File;
import javax.swing.UIManager;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class JFiglet {
   private JFiglet() {
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var2) {
      }

      try {
         startUp(args);
      } catch (Throwable exception) {
         handleStartupFailure(exception);
      }
   }

   private static void startUp(String[] args) throws FigException {
      File codeBase = CodeBaseTool.getCodeBase(JFiglet.class);
      Figlet figlet;
      if (args.length == 0) {
         File fontFolder = null;

         try {
            fontFolder = new File(codeBase, "fonts");
            figlet = new Figlet(fontFolder);
         } catch (FigException exception) {
            if (fontFolder != null) {
               throw new FigException(
                  "Unable to load FIGlet fonts from program folder '"
                     + fontFolder.getAbsolutePath()
                     + "'.\n"
                     + "You can either install the FIGlet font library to the program folder or specify\n"
                     + "a font library location as program argument.\n"
                     + "Exiting.",
                  exception
               );
            }

            throw new FigException(
               "Unable to determin the FIGlet fonts folder.\nYou can either install the FIGlet font library to the program folder or specify\na font library location as program argument.\nExiting.",
               exception
            );
         }
      } else {
         if (args.length != 1) {
            throw new FigException("Invalid arguments. Only one optional argument pointing to the\nFIGlet fonts library is supported.");
         }

         try {
            figlet = new Figlet(args[0]);
         } catch (FigException exception) {
            throw new FigException(
               "Unable to load FIGlet fonts from the specified lirary location '"
                  + args[0]
                  + "'.\n"
                  + "You can either install the FIGlet font library to the program folder or specify\n"
                  + "a valid font library location as program argument.\n"
                  + "Exiting.",
               exception
            );
         }
      }

      File tmpFolder = new File(codeBase, "tmp");
      JFigletApplication application = new JFigletApplication(figlet.getFigDriver(), tmpFolder);
      application.start();
   }

   private static void handleStartupFailure(Throwable exception) {
      exception.printStackTrace();
      String message = exception.getLocalizedMessage();
      if (message == null || message.length() == 0) {
         message = exception.toString();
      }
      MessageDialogFactory.showMessageDialog(null, new Message(message, exception));
      System.exit(1);
   }
}
