package net.dizzy.commons.swing.dialog.wizard;

import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;

public interface IWizardPage {
   String getTitle();
   boolean canFlipToNextPage();
   boolean canFinish();
   void requestFocus();
   IDialogHelpHandler getHelpHandler();
   void checkInputValid();
   IBasicMessage getDefaultMessage();
   IBasicMessage getMessage();
   IWizardPage getNextPage();
   void performHelp();
   boolean isHelpAvailable();
   default void enter() {
   }

   default void setWizard(IWizardConfiguration wizard) {
   }
}
