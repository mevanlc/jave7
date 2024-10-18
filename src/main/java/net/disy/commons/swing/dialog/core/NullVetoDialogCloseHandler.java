package net.disy.commons.swing.dialog.core;

import java.awt.Component;

public final class NullVetoDialogCloseHandler implements IVetoDialogCloseHandler {
   @Override
   public boolean handleDialogAboutToClose(IDialogResult result, Component parentComponent) {
      return true;
   }
}
