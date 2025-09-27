package net.disy.commons.swing.dialog.wizard.managed;

import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.dialog.input.IRequestFinishListener;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.dialog.wizard.AbstractWizardPage;
import net.disy.commons.swing.dialog.wizard.IWizardConfiguration;

public class DialogWizardPageAdapter extends AbstractWizardPage implements IManagedWizardPage {
   private final IDialogPage dialogPage;

   public DialogWizardPageAdapter(IDialogPage dialogPage, IWizardConfiguration wizard) {
      super(dialogPage.getDescription(), dialogPage.getTitle(), dialogPage.getDefaultMessage().getText(), wizard);
      this.dialogPage = dialogPage;
      dialogPage.setInputValidListener(this.getCheckInputValidListener());
      dialogPage.addRequestFinishListener(new IRequestFinishListener() {
         @Override
         public void requestFinish() {
            DialogWizardPageAdapter.this.fireRequestFinish();
         }
      });
      this.addDisposable(dialogPage);
   }

   @Override
   protected final IBasicMessage createCurrentMessage() {
      return this.dialogPage.createCurrentMessage();
   }

   @Override
   protected final JComponent createContent() {
      return this.dialogPage.createContent();
   }

   @Override
   public IDialogHelpHandler getHelpHandler() {
      return this.dialogPage.getHelpHandler();
   }

   @Override
   public final void requestFocus() {
      this.dialogPage.requestFocus();
   }

   @Override
   public boolean isApplicable() {
      return true;
   }

   @Override
   public void enter() {
      this.dialogPage.enter();
   }

   @Override
   public void leave() {
      this.dialogPage.leave();
   }

   @Override
   public boolean canFinish() {
      return this.dialogPage.canFinish();
   }
}
