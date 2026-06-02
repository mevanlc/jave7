package net.dizzy.commons.swing.dialog.wizard;

import javax.swing.JComponent;

import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;

class WizardPageAdapter extends net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage {
   private final IWizardPage page;

   WizardPageAdapter(IWizardPage page) {
      super(page.getDefaultMessage() == null ? "" : page.getDefaultMessage().getText());
      this.page = page;
   }

   @Override protected JComponent createContent() { return ((AbstractWizardPage) page).createWizardContent(); }
   @Override public String getTitle() { return page.getTitle(); }
   @Override protected IBasicMessage createCurrentMessage() { return ((AbstractWizardPage) page).createWizardCurrentMessage(); }
   @Override public IBasicMessage getDefaultMessage() { return page.getDefaultMessage(); }
   @Override public void checkInputValid() { page.checkInputValid(); }
   @Override public IDialogHelpHandler getHelpHandler() { return page.getHelpHandler(); }
}
