package net.dizzy.commons.swing.dialog.wizard;

import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;

public abstract class AbstractWizardPage extends AbstractChangeableModel implements IWizardPage {
   private final String title;
   private final IBasicMessage defaultMessage;
   private IBasicMessage message;
   private AbstractWizardConfiguration wizard;

   public AbstractWizardPage(String title, String defaultMessageText) {
      this.title = title;
      defaultMessage = new BasicMessage(defaultMessageText, MessageType.NORMAL);
      message = defaultMessage;
   }

   public AbstractWizardPage(String title, String headerText, String defaultMessageText) {
      this(title, defaultMessageText);
   }

   protected abstract javax.swing.JComponent createContent();

   protected abstract IBasicMessage createCurrentMessage();

   public javax.swing.JComponent createWizardContent() {
      return createContent();
   }

   public IBasicMessage createWizardCurrentMessage() {
      return createCurrentMessage();
   }

   public void setWizard(AbstractWizardConfiguration wizard) { this.wizard = wizard; }
   @Override public void setWizard(IWizardConfiguration wizard) { this.wizard = wizard instanceof AbstractWizardConfiguration ? (AbstractWizardConfiguration) wizard : null; }
   public AbstractWizardConfiguration getWizard() { return wizard; }
   @Override public String getTitle() { return title; }
   @Override public IBasicMessage getDefaultMessage() { return defaultMessage; }
   @Override public IBasicMessage getMessage() { return message; }
   @Override public void checkInputValid() { message = createCurrentMessage(); fireChangeEvent(); }
   @Override public boolean canFlipToNextPage() { return getNextPage() != null; }
   @Override public boolean canFinish() { return false; }
   @Override public void requestFocus() { }
   @Override public IDialogHelpHandler getHelpHandler() { return null; }
   @Override public IWizardPage getNextPage() { return wizard == null ? null : wizard.getNextPage(this); }

   @Override public void performHelp() { }
   @Override public boolean isHelpAvailable() { return false; }

   public net.dizzy.commons.core.model.listener.IChangeListener getCheckInputValidListener() {
      return new net.dizzy.commons.core.model.listener.IChangeListener() {
         @Override
         public void stateChanged() {
            checkInputValid();
         }
      };
   }
}
