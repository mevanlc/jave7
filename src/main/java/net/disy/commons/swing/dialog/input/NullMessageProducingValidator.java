package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.IBasicMessage;

public class NullMessageProducingValidator implements IMessageProducingValidator {
   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return null;
   }
}
