package de.jave.gui.dialog;

import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;

public class HtmlMessage {
   private final MessageType type;
   private final String htmlText;
   private final String title;

   public HtmlMessage(String title, String htmlText, MessageType type) {
      Ensure.ensureArgumentNotNull(title);
      Ensure.ensureArgumentNotNull(htmlText);
      Ensure.ensureArgumentNotNull(type);
      this.title = title;
      this.htmlText = htmlText;
      this.type = type;
   }

   public String getHtmlText() {
      return this.htmlText;
   }

   public MessageType getType() {
      return this.type;
   }

   public String getTitle() {
      return this.title;
   }
}
