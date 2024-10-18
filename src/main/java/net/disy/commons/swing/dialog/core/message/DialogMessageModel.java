package net.disy.commons.swing.dialog.core.message;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class DialogMessageModel extends AbstractChangeableModel {
   private IBasicMessage baseMessage;
   private IBasicMessage overlaidMessage;
   private boolean overlaidMessageActive = false;

   public void setMessage(IBasicMessage message) {
      Ensure.ensureArgumentNotNull(message);
      synchronized (this.getMutex()) {
         if (!message.equals(this.getMessage())) {
            if (message.getType() == MessageType.NORMAL) {
               this.baseMessage = message;
               this.overlaidMessageActive = false;
            } else {
               this.overlaidMessage = message;
               this.overlaidMessageActive = true;
            }

            this.fireChangeEvent();
         }
      }
   }

   public boolean isOverlaidMessageActive() {
      synchronized (this.getMutex()) {
         return this.overlaidMessageActive;
      }
   }

   public IBasicMessage getBaseMessage() {
      synchronized (this.getMutex()) {
         return this.baseMessage;
      }
   }

   public IBasicMessage getOverlaidMessage() {
      synchronized (this.getMutex()) {
         return this.overlaidMessage;
      }
   }

   public IBasicMessage getMessage() {
      synchronized (this.getMutex()) {
         return this.overlaidMessageActive ? this.overlaidMessage : this.baseMessage;
      }
   }
}
