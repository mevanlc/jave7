package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.StringUtilities;

public class NonEmptyStringMessageProducingValidator implements IMessageProducingValidator {
   private final ObjectModel<String> model;
   private final String errorMessageText;

   public NonEmptyStringMessageProducingValidator(ObjectModel<String> model, String errorMessageText) {
      this.model = model;
      this.errorMessageText = errorMessageText;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return StringUtilities.isNullOrEmpty(this.model.getValue()) ? new BasicMessage(this.errorMessageText, MessageType.ERROR) : null;
   }
}
