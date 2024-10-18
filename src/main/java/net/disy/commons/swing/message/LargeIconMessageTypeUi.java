package net.disy.commons.swing.message;

import javax.swing.Icon;
import net.disy.commons.core.message.IMessageTypeVisitor;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.icon.EmptyIcon;
import net.disy.commons.swing.icon.SwingIcons;

public class LargeIconMessageTypeUi extends AbstractMessageTypeUi {
   public Icon getIcon(MessageType type) {
      final Icon[] icon = new Icon[1];
      type.accept(new IMessageTypeVisitor() {
         @Override
         public void visitError(MessageType visitedType) {
            icon[0] = SwingIcons.getOptionPaneErrorIcon();
         }

         @Override
         public void visitNormal(MessageType visitedType) {
            icon[0] = new EmptyIcon();
         }

         @Override
         public void visitWarning(MessageType visitedType) {
            icon[0] = SwingIcons.getOptionPaneWarningIcon();
         }

         @Override
         public void visitInformation(MessageType visitedType) {
            icon[0] = SwingIcons.getOptionPaneInformationIcon();
         }

         @Override
         public void visitQuestion(MessageType visitedType) {
            icon[0] = SwingIcons.getOptionPaneQuestionIcon();
         }
      });
      return icon[0];
   }
}
