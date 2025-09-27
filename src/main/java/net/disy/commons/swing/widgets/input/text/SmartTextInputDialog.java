package net.disy.commons.swing.dialog.input.text;

import java.awt.Component;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class SmartTextInputDialog {
   private SmartTextInputDialog() {
   }

   public static ITextInputDialogResult showTextInputDialog(Component parentComponent, ITextInputDialogConfiguration configuration, String initialText) {
      TextInputDialogPage page = new TextInputDialogPage(configuration, initialText);
      UserDialog dialog = new UserDialog(parentComponent, page);
      final IDialogResult result = dialog.show();
      final String selectedText = page.getSelectedText();
      return new ITextInputDialogResult() {
         @Override
         public boolean isCanceled() {
            return result.isCanceled();
         }

         @Override
         public String getText() {
            return selectedText;
         }
      };
   }

   public static UserDialog createDemoDialog(Component parentComponent, ITextInputDialogConfiguration configuration, String initialText) {
      TextInputDialogPage page = new TextInputDialogPage(configuration, initialText);
      return new UserDialog(parentComponent, page);
   }
}
