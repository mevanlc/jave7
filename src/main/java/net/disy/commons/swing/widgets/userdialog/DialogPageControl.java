package net.disy.commons.swing.dialog.userdialog;

import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.ContractFailedException;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;
import net.disy.commons.swing.dialog.core.DialogPageInputValidCheckable;
import net.disy.commons.swing.dialog.core.IDialogControl;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.events.CheckInputValidListener;
import net.disy.commons.swing.events.IInputValidCheckable;

public class DialogPageControl implements IInputValidCheckable, IMessageSetable {
   private IDialogControl dialogControl = new NullDialogControl();
   private JComponent content;
   private final IDialogPage dialogPage;
   private IBasicMessage message;

   public DialogPageControl(IDialogPage dialogPage) {
      this.dialogPage = dialogPage;
      this.setMessage(dialogPage.getDefaultMessage());
   }

   public boolean canFinish() {
      return this.dialogPage.canFinish();
   }

   @Override
   public final void setMessage(IBasicMessage message) {
      Ensure.ensureArgumentNotNull(message);
      if (ObjectUtilities.equals(this.message, message)) {
         this.updateCanFinish();
      } else {
         this.message = message;
         this.updateCanFinish();
         this.dialogControl.updateMessage();
      }
   }

   private void updateCanFinish() {
      this.dialogControl.updateButtons();
   }

   public final IBasicMessage getMessage() {
      return this.message;
   }

   public final void setDialogControl(IDialogControl dialogControl) {
      this.dialogControl = dialogControl;
   }

   public final JComponent getContent() {
      if (this.content == null) {
         this.content = this.dialogPage.createContent();
         if (this.content == null) {
            throw new ContractFailedException("Method createContent() must not return null in " + this.dialogPage.getClass().getName());
         }

         this.dialogPage.setInputValidListener(new CheckInputValidListener(new DialogPageInputValidCheckable(this, this.dialogPage)));
         this.updateButtons();
      }

      return this.content;
   }

   protected void updateButtons() {
      this.dialogControl.updateButtons();
   }

   @Deprecated
   public boolean performOk() {
      return this.dialogPage.performOk();
   }

   @Deprecated
   public boolean performCancel() {
      return this.dialogPage.performCancel();
   }

   public String getDescription() {
      return this.dialogPage.getDescription();
   }

   @Override
   public void checkInputValid() {
      this.setMessage(this.dialogPage.createCurrentMessage());
      this.dialogPage.updateInputValid();
   }

   public String getTitle() {
      return this.dialogPage.getTitle();
   }

   public void requestFocus() {
      this.dialogPage.requestFocus();
   }

   public IDialogHelpHandler getHelpHandler() {
      return this.dialogPage.getHelpHandler();
   }
}
