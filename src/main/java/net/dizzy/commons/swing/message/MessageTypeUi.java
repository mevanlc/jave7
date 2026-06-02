package net.dizzy.commons.swing.message;

import javax.swing.Icon;
import javax.swing.UIManager;

import net.dizzy.commons.core.message.MessageType;

public final class MessageTypeUi {
   public static final Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");

   private static final MessageTypeUi INSTANCE = new MessageTypeUi();

   public MessageTypeUi() {
   }

   public static MessageTypeUi getInstance() {
      return INSTANCE;
   }

   public static Icon getIcon(MessageType type) {
      return UIManager.getIcon(type == MessageType.ERROR ? "OptionPane.errorIcon" : "OptionPane.informationIcon");
   }

   public String getLabel(MessageType type) {
      return type == null ? "" : type.name();
   }
}
