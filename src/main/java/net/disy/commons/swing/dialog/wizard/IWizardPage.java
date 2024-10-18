package net.disy.commons.swing.dialog.wizard;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.core.IPage;
import net.disy.commons.swing.dialog.core.IPageContent;

public interface IWizardPage extends IPage {
   IBasicMessage getMessage();

   IPageContent getPageContent();

   boolean canFlipToNextPage();

   IWizardPage getNextPage();

   IWizardPage getPreviousPage();

   boolean canCancel();

   void setWizard(IWizardConfiguration var1);

   @Deprecated
   IWizardConfiguration getWizard();

   void setMessage(IBasicMessage var1);

   @Deprecated
   void initializeFromData();
}
