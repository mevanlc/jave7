package net.dizzy.commons.swing.dialog.userdialog.page;

import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;

public abstract class AbstractDialogPage extends AbstractChangeableModel implements IDialogPage {
   private final IBasicMessage defaultMessage;
   private IBasicMessage currentMessage;

   public AbstractDialogPage(String defaultMessageText) {
      defaultMessage = new BasicMessage(defaultMessageText, MessageType.NORMAL);
      currentMessage = defaultMessage;
   }

   protected abstract javax.swing.JComponent createContent();

   protected abstract IBasicMessage createCurrentMessage();

   public javax.swing.JComponent createDialogContent() {
      return createContent();
   }

   public IBasicMessage createDialogCurrentMessage() {
      return createCurrentMessage();
   }

   @Override
   public IBasicMessage getDefaultMessage() {
      return defaultMessage;
   }

   public IBasicMessage getMessage() {
      return currentMessage;
   }

   @Override
   public void checkInputValid() {
      currentMessage = createCurrentMessage();
      fireChangeEvent();
   }

   public net.dizzy.commons.core.model.listener.IChangeListener getCheckInputValidListener() {
      return new net.dizzy.commons.core.model.listener.IChangeListener() {
         @Override
         public void stateChanged() {
            checkInputValid();
         }
      };
   }

   protected void fireRequestFinish() {
      fireChangeEvent();
   }

   public void requestFocus() {
   }

   @Override
   public IDialogHelpHandler getHelpHandler() {
      return null;
   }
}
