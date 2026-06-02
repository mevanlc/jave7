package net.dizzy.commons.core.exception;

import net.dizzy.commons.core.message.Message;

public class MessageException extends RuntimeException {
   private final Message messageObject;

   public MessageException(Message message) {
      super(message.getDetailedText(), message.getCause());
      this.messageObject = message;
   }

   public Message getMessageObject() {
      return messageObject;
   }
}
