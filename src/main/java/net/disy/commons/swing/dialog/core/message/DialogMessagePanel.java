package net.disy.commons.swing.dialog.core.message;

import javax.swing.JComponent;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.animation.AnimatedCompositeComponent;
import net.disy.commons.swing.dialog.animation.OverlaidComponentBorder;

public class DialogMessagePanel {
   private final DialogMessageModel messageModel;
   private final DialogMessageComponent baseMessageComponent;
   private final DialogMessageComponent overlaidMessageComponent;
   private final AnimatedCompositeComponent content;

   public DialogMessagePanel(DialogMessageModel messageModel) {
      Ensure.ensureArgumentNotNull(messageModel);
      this.messageModel = messageModel;
      this.baseMessageComponent = new DialogMessageComponent(false);
      this.overlaidMessageComponent = new DialogMessageComponent(true);
      this.overlaidMessageComponent.setBorder(new OverlaidComponentBorder());
      this.content = new AnimatedCompositeComponent(this.baseMessageComponent, this.overlaidMessageComponent);
      messageModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            DialogMessagePanel.this.updateMessage();
         }
      });
      this.updateMessage();
   }

   private void updateMessage() {
      this.baseMessageComponent.setMessage(this.messageModel.getBaseMessage());
      this.overlaidMessageComponent.setMessage(this.messageModel.getOverlaidMessage());
      this.content.setOverlaidComponentVisible(this.messageModel.isOverlaidMessageActive());
   }

   public JComponent getContent() {
      return this.content;
   }
}
