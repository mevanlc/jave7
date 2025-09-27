package net.disy.commons.swing.dialog.wizard.managed;

import net.disy.commons.swing.dialog.wizard.IWizardPage;

public class EmptyWizardPageManager implements IWizardPageManager {
   @Override
   public IWizardPage getNextPage(IWizardPage page) {
      return null;
   }

   @Override
   public IWizardPage getPreviousPage(IWizardPage page) {
      return null;
   }

   @Override
   public IWizardPage getStartPage() {
      return null;
   }
}
