package de.jave.gui.dialog;

import de.jave.lib.gui.GuiUtilities;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JDialog;

@Deprecated
public class JDialogFactory {
   public static JDialog createJDialog(Component parent, String title, boolean modal) {
      Window parentWindow = GuiUtilities.getWindowForComponent(parent);
      JDialog dialog;
      if (parentWindow instanceof Frame) {
         dialog = new JDialog((Frame)parentWindow, title, modal);
      } else if (parentWindow instanceof Dialog) {
         dialog = new JDialog((Dialog)parentWindow, title, modal);
      } else {
         dialog = new JDialog((Frame)null, title, modal);
      }

      dialog.setDefaultCloseOperation(0);
      return dialog;
   }
}
