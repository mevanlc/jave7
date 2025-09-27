package net.disy.commons.swing.dialog.wizard.managed;

import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.dialog.wizard.IWizardConfiguration;

public final class ErrorRejectingDialogWizardPageAdapter extends DialogWizardPageAdapter {
   public ErrorRejectingDialogWizardPageAdapter(IDialogPage page, IWizardConfiguration wizard) {
      super(page, wizard);
   }

   @Override
   public boolean canFinish() {
      return !this.createCurrentMessage().isErrorMessage();
   }
}
