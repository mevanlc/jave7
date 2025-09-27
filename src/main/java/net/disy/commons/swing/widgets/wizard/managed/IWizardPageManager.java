package net.disy.commons.swing.dialog.wizard.managed;

import net.disy.commons.swing.dialog.wizard.IWizardPage;

public interface IWizardPageManager {
   IWizardPage getStartPage();

   IWizardPage getNextPage(IWizardPage var1);

   IWizardPage getPreviousPage(IWizardPage var1);
}
