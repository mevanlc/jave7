package net.dizzy.commons.swing.dialog.wizard;

import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IVetoDialogCloseHandler;

public interface IWizardConfiguration {
   void addPages();
   IWizardPage getStartingPage();
   IWizardPage getNextPage(IWizardPage page);
   IWizardPage getPreviousPage(IWizardPage page);
   boolean isHelpAvailable();
   IDialogHeaderPanelConfiguration getHeaderPanelConfiguration();
   IVetoDialogCloseHandler getVetoCloseHandler();
}
