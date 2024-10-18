package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;

public class RangeCheckingValidator implements IMessageProducingValidator {
   private final ObjectModel<Integer> minResultCountModel;
   private final ObjectModel<Integer> maxResultCountModel;
   private final String messageText;

   public RangeCheckingValidator(ObjectModel<Integer> minResultCountModel, ObjectModel<Integer> maxResultCountModel, String messageText) {
      this.minResultCountModel = minResultCountModel;
      this.maxResultCountModel = maxResultCountModel;
      this.messageText = messageText;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      if (this.minResultCountModel.getValue() == null) {
         return null;
      } else if (this.maxResultCountModel.getValue() == null) {
         return null;
      } else {
         return this.maxResultCountModel.getValue() < this.minResultCountModel.getValue() ? new BasicMessage(this.messageText, MessageType.ERROR) : null;
      }
   }
}
