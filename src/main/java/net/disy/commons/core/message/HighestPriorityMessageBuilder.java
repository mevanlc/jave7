package net.disy.commons.core.message;

public class HighestPriorityMessageBuilder {
   private IBasicMessage highestPriorityMessage;

   public IBasicMessage getHighestPriorityMessage() {
      return this.highestPriorityMessage;
   }

   public void addMessage(IBasicMessage message) {
      if (message != null) {
         if (this.highestPriorityMessage == null || isHigherPriorityThan(message, this.highestPriorityMessage)) {
            this.highestPriorityMessage = message;
         }
      }
   }

   private static boolean isHigherPriorityThan(IBasicMessage message1, IBasicMessage message2) {
      return message1.getType().getPriority().compareTo(message2.getType().getPriority()) > 0;
   }
}
