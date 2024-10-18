package net.disy.commons.swing.message;

import javax.swing.Icon;
import net.disy.commons.core.message.IMessageTypeVisitor;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.icon.EmptyIcon;
import net.disy.commons.swing.image.DisyCommonsSwingImageProvider;

public class MessageTypeUi extends AbstractMessageTypeUi {
   public static final Icon errorIcon = DisyCommonsSwingImageProvider.getInstance().getImageIcon("message/small/error.gif");
   public static final Icon warningIcon = DisyCommonsSwingImageProvider.getInstance().getImageIcon("message/small/warning.gif");
   public static final Icon infoIcon = DisyCommonsSwingImageProvider.getInstance().getImageIcon("message/small/info.gif");
   public static final Icon normalIcon = EmptyIcon.DEFAULT_ICON;
   public static final Icon questionIcon = DisyCommonsSwingImageProvider.getInstance().getImageIcon("message/small/question.gif");
   private static MessageTypeUi instance = new MessageTypeUi();

   public Icon getIcon(MessageType type) {
      final Icon[] icon = new Icon[1];
      type.accept(new IMessageTypeVisitor() {
         @Override
         public void visitError(MessageType visitedType) {
            icon[0] = MessageTypeUi.errorIcon;
         }

         @Override
         public void visitNormal(MessageType visitedType) {
            icon[0] = MessageTypeUi.normalIcon;
         }

         @Override
         public void visitWarning(MessageType visitedType) {
            icon[0] = MessageTypeUi.warningIcon;
         }

         @Override
         public void visitInformation(MessageType visitedType) {
            icon[0] = MessageTypeUi.infoIcon;
         }

         @Override
         public void visitQuestion(MessageType visitedType) {
            icon[0] = MessageTypeUi.questionIcon;
         }
      });
      return icon[0];
   }

   private MessageTypeUi() {
   }

   public static MessageTypeUi getInstance() {
      return instance;
   }
}
