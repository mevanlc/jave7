package net.disy.commons.swing.dialog.core;

public interface IDialogContainer extends IDialogControl {
   IDialogResult show();

   void requestFinish();
}
