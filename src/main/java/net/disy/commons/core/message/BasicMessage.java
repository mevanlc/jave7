package net.disy.commons.core.message;

import net.disy.commons.core.util.Ensure;

public class BasicMessage implements IBasicMessage {
   private final MessageType type;
   private final String text;

   public BasicMessage(String text) {
      this(text, MessageType.NORMAL);
   }

   public BasicMessage(String text, MessageType type) {
      Ensure.ensureArgumentNotNull("Text for message may not be null.", text);
      Ensure.ensureArgumentNotNull("Type for message may not be null.", type);
      this.text = text;
      this.type = type;
   }

   @Override
   public String getText() {
      return this.text;
   }

   @Override
   public MessageType getType() {
      return this.type;
   }

   @Override
   public boolean isErrorMessage() {
      return MessageType.ERROR.equals(this.type);
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof BasicMessage)) {
         return false;
      } else {
         BasicMessage other = (BasicMessage)obj;
         return other.type == this.type && this.text.equals(other.text);
      }
   }

   @Override
   public int hashCode() {
      return this.text.hashCode() + 5 * this.type.hashCode();
   }

   @Override
   public String toString() {
      return this.getType() + ": " + this.getText();
   }
}
