package net.disy.commons.swing.dialog.wizard;

import java.awt.Component;
import net.disy.commons.swing.dialog.core.IGenericDialogConfiguration;

public interface IWizardConfiguration extends IGenericDialogConfiguration {
   IWizardPage getStartingPage();

   IWizardContainer getContainer();

   void setContainer(IWizardContainer var1);

   boolean canFinish();

   boolean canCancel();

   boolean isHelpAvailable();

   void addPages();

   IWizardPage getNextPage(IWizardPage var1);

   IWizardPage getPreviousPage(IWizardPage var1);

   @Deprecated
   boolean performFinish(Component var1);

   boolean shallInitializePagesFromData();

   void updateSize();
}
