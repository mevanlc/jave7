package net.disy.commons.core.exception;

import net.disy.commons.core.message.IMessage;

public class MessageRuntimeException extends RuntimeException {
   private final IMessage message;

   public MessageRuntimeException(IMessage message) {
      super(message.getText());
      this.message = message;
   }

   public IMessage getMessageObject() {
      return this.message;
   }
}
