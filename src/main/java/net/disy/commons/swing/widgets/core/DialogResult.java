package net.disy.commons.swing.dialog.core;

public class DialogResult implements IDialogResult {
   private final boolean canceled;

   public DialogResult(boolean canceled) {
      this.canceled = canceled;
   }

   @Override
   public boolean isCanceled() {
      return this.canceled;
   }
}
