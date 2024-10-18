package de.jave.gui.dialog;

import java.awt.Component;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class HtmlMessageDialogFactory {
   public static void showMessageDialog(Component parentComponent, HtmlMessage message) {
      UserDialog dialog = new UserDialog(parentComponent, new HtmlMessageDialog(message));
      dialog.show();
   }
}
