package net.disy.commons.core.message;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Message extends BasicMessage implements IMessage {
   private final Throwable throwable;
   private final String title;
   private final String detailText;

   public static IMessage createWarning(String message) {
      return new Message(message, MessageType.WARNING);
   }

   private Message(String title, String text, MessageType type, String detailText, Throwable throwable) {
      super(text, type);
      this.throwable = throwable;
      this.title = title;
      this.detailText = detailText;
   }

   public Message(String title, String text, MessageType type, Throwable throwable) {
      this(title, text, type, getStackTrace(throwable), throwable);
   }

   private static String getStackTrace(Throwable throwable) {
      if (throwable == null) {
         return null;
      } else {
         StringWriter stacktrace = new StringWriter();
         throwable.printStackTrace(new PrintWriter(stacktrace));
         return stacktrace.toString();
      }
   }

   public Message(String title, String text, Throwable throwable) {
      this(title, text, MessageType.ERROR, throwable);
   }

   public Message(String title, String text, MessageType type) {
      this(title, text, type, null);
   }

   public Message(String text, MessageType type, Throwable throwable) {
      this(null, text, type, throwable);
   }

   public Message(String text, Throwable throwable) {
      this(null, text, throwable);
   }

   public Message(String text, MessageType type) {
      this(text, type, (String)null);
   }

   public Message(String text, MessageType messageType, String detailText) {
      this(null, text, messageType, detailText, null);
   }

   @Deprecated
   @Override
   public String getTitle() {
      return this.title;
   }

   @Override
   public Throwable getThrowable() {
      return this.throwable;
   }

   @Override
   public String getDetail() {
      return this.detailText;
   }

   @Override
   public String getDetailedText() {
      return this.getText() + (this.throwable == null && this.detailText != null ? " - " + this.detailText : "");
   }
}
