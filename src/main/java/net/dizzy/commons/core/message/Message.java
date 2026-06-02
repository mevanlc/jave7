package net.dizzy.commons.core.message;

public class Message extends BasicMessage implements IMessage {
   private final Throwable cause;

   public Message(String text, MessageType type) {
      this(null, text, type, null);
   }

   public Message(String text, Throwable cause) {
      this(null, text, MessageType.ERROR, cause);
   }

   public Message(String title, String text, MessageType type) {
      this(title, text, type, null);
   }

   public Message(String title, String text, Throwable cause) {
      this(title, text, MessageType.ERROR, cause);
   }

   public Message(String title, String text, MessageType type, Throwable cause) {
      super(title, text, type);
      this.cause = cause;
   }

   public Throwable getCause() {
      return cause;
   }

   @Override
   public String getDetailedText() {
      String text = getText() == null ? "" : getText();
      if (cause == null) {
         return text;
      }
      String causeMessage = cause.getMessage();
      if (causeMessage == null || causeMessage.isEmpty()) {
         return text;
      }
      return text.isEmpty() ? causeMessage : text + "\n" + causeMessage;
   }
}
