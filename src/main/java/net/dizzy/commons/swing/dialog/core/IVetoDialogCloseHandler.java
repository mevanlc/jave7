package net.dizzy.commons.swing.dialog.core;

import java.awt.Component;

public interface IVetoDialogCloseHandler {
   boolean handleDialogAboutToClose(IDialogResult result, Component parent);
}
