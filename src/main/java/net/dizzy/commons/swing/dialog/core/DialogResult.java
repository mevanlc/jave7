package net.dizzy.commons.swing.dialog.core;

public class DialogResult implements IDialogResult {
   public static final DialogResult OK = new DialogResult(false);
   public static final DialogResult CANCELED = new DialogResult(true);

   private final boolean canceled;

   public DialogResult(boolean canceled) {
      this.canceled = canceled;
   }

   @Override
   public boolean isCanceled() {
      return canceled;
   }
}
