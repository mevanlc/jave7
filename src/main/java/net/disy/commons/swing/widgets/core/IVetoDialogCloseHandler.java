package net.disy.commons.swing.dialog.core;

import java.awt.Component;

public interface IVetoDialogCloseHandler {
   IVetoDialogCloseHandler NULL_HANDLER = new NullVetoDialogCloseHandler();

   boolean handleDialogAboutToClose(IDialogResult var1, Component var2);
}
