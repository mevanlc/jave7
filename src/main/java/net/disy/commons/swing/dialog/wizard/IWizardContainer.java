package net.disy.commons.swing.dialog.wizard;

import net.disy.commons.swing.dialog.core.IDialogContainer;

public interface IWizardContainer extends IDialogContainer {
   void showPage(IWizardPage var1);

   IWizardPage getCurrentPage();

   void updateSize();

   void requestNext();
}
