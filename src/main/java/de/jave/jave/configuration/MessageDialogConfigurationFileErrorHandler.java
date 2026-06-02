package de.jave.jave.configuration;

import java.awt.Component;
import net.dizzy.commons.core.message.IMessage;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class MessageDialogConfigurationFileErrorHandler implements IConfigurationFileErrorHandler {
   private final Component parentComponent;

   public MessageDialogConfigurationFileErrorHandler(Component parentComponent) {
      this.parentComponent = parentComponent;
   }

   @Override
   public void handleError(IMessage message) {
      MessageDialogFactory.showMessageDialog(this.parentComponent, message);
   }
}
