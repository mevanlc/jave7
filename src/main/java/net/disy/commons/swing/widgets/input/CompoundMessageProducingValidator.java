package net.disy.commons.swing.dialog.input;

import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.message.HighestPriorityMessageBuilder;
import net.disy.commons.core.message.IBasicMessage;

public class CompoundMessageProducingValidator implements IMessageProducingValidator {
   private final List<IMessageProducingValidator> validators = new ArrayList<>();

   public void addValidator(IMessageProducingValidator validator) {
      this.validators.add(validator);
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      HighestPriorityMessageBuilder builder = new HighestPriorityMessageBuilder();

      for (IMessageProducingValidator validator : this.validators) {
         builder.addMessage(validator.createOptionalCurrentMessage());
      }

      return builder.getHighestPriorityMessage();
   }
}
