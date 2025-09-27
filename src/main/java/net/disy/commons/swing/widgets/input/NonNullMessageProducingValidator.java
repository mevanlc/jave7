package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;

public class NonNullMessageProducingValidator<T> implements IMessageProducingValidator {
   private final ObjectModel<T> model;
   private final String errorMessageText;

   public NonNullMessageProducingValidator(ObjectModel<T> model, String errorMessageText) {
      this.model = model;
      this.errorMessageText = errorMessageText;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return this.model.getValue() == null ? new BasicMessage(this.errorMessageText, MessageType.ERROR) : null;
   }
}
