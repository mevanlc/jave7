package net.disy.commons.swing.dialog.userdialog;

import net.disy.commons.swing.dialog.core.IDialogResult;

public interface IDialogCloseHandler {
   IDialogCloseHandler NULL_HANDLER = new IDialogCloseHandler() {
      @Override
      public void handleDialogClose(IDialogResult result) {
      }
   };

   void handleDialogClose(IDialogResult var1);
}
