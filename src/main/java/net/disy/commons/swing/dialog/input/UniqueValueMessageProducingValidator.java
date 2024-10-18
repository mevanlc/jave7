package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.ArrayUtilities;

public class UniqueValueMessageProducingValidator<T> implements IMessageProducingValidator {
   private final ObjectModel<T> model;
   private final String errorMessageText;
   private final T[] values;

   public UniqueValueMessageProducingValidator(ObjectModel<T> model, T[] values, String errorMessageText) {
      this.model = model;
      this.values = values;
      this.errorMessageText = errorMessageText;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return ArrayUtilities.containsValue(this.values, this.model.getValue()) ? new BasicMessage(this.errorMessageText, MessageType.ERROR) : null;
   }
}
