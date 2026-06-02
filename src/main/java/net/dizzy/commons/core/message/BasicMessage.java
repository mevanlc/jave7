package net.dizzy.commons.core.message;

public class BasicMessage implements IBasicMessage {
   private final String title;
   private final String text;
   private final MessageType type;

   public BasicMessage(String text, MessageType type) {
      this(null, text, type);
   }

   public BasicMessage(String title, String text, MessageType type) {
      this.title = title;
      this.text = text;
      this.type = type;
   }

   @Override
   public MessageType getType() {
      return type;
   }

   @Override
   public String getText() {
      return text;
   }

   @Override
   public String getTitle() {
      return title;
   }
}
