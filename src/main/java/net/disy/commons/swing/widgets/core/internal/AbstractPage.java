package net.disy.commons.swing.dialog.core.internal;

import java.awt.Component;
import net.disy.commons.swing.component.DisposableContainer;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.dialog.core.IPage;

public abstract class AbstractPage extends DisposableContainer implements IPage {
   @Deprecated
   protected void performHelp() {
      throw new UnsupportedOperationException();
   }

   @Deprecated
   protected boolean isHelpAvailable() {
      return false;
   }

   @Override
   public IDialogHelpHandler getHelpHandler() {
      return this.isHelpAvailable() ? new IDialogHelpHandler() {
         @Override
         public void execute(Component parentComponent) {
            AbstractPage.this.performHelp();
         }
      } : null;
   }
}
