package net.disy.commons.swing.message;

import java.awt.Color;
import net.disy.commons.core.message.IMessageTypeVisitor;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;
import net.disy.commons.swing.ui.AbstractObjectUi;

public abstract class AbstractMessageTypeUi extends AbstractObjectUi<MessageType> {
   public static Color getColor(MessageType type) {
      final Color[] color = new Color[1];
      type.accept(new IMessageTypeVisitor() {
         @Override
         public void visitError(MessageType visitedType) {
            color[0] = Color.red;
         }

         @Override
         public void visitNormal(MessageType visitedType) {
            color[0] = SwingColors.getTextAreaForegroundColor();
         }

         @Override
         public void visitWarning(MessageType visitedType) {
            color[0] = SwingColors.getTextAreaForegroundColor();
         }

         @Override
         public void visitInformation(MessageType visitedType) {
            color[0] = SwingColors.getTextAreaForegroundColor();
         }

         @Override
         public void visitQuestion(MessageType visitedType) {
            color[0] = SwingColors.getTextAreaForegroundColor();
         }
      });
      return color[0];
   }

   public final String getLabel(MessageType type) {
      final String[] label = new String[1];
      type.accept(new IMessageTypeVisitor() {
         @Override
         public void visitInformation(MessageType visitedType) {
            label[0] = DisyCommonsSwingMessages.getString("MessageTypeUi.information.label");
         }

         @Override
         public void visitWarning(MessageType visitedType) {
            label[0] = DisyCommonsSwingMessages.getString("MessageTypeUi.warning.label");
         }

         @Override
         public void visitNormal(MessageType visitedType) {
            label[0] = DisyCommonsSwingMessages.getString("MessageTypeUi.normal.label");
         }

         @Override
         public void visitError(MessageType visitedType) {
            label[0] = DisyCommonsSwingMessages.getString("MessageTypeUi.error.label");
         }

         @Override
         public void visitQuestion(MessageType visitedType) {
            label[0] = DisyCommonsSwingMessages.getString("MessageTypeUi.question.label");
         }
      });
      return label[0];
   }
}
