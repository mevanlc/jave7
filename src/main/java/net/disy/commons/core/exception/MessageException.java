package net.disy.commons.core.exception;

import net.disy.commons.core.message.IMessage;

public class MessageException extends Exception {
   private final IMessage message;

   public MessageException(IMessage message) {
      super(message.getText(), message.getThrowable());
      this.message = message;
   }

   public IMessage getMessageObject() {
      return this.message;
   }
}
