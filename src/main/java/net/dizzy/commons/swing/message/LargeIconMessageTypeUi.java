package net.dizzy.commons.swing.message;

import javax.swing.Icon;

import net.dizzy.commons.core.message.MessageType;

public final class LargeIconMessageTypeUi {
   public LargeIconMessageTypeUi() {
   }

   public static Icon getIcon(MessageType type) {
      return MessageTypeUi.getIcon(type);
   }
}
