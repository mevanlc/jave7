package net.disy.commons.core.message;

public enum MessageType {
   ERROR("Fehler", 4) {
      @Override
      public void accept(IMessageTypeVisitor visitor) {
         visitor.visitError(this);
      }
   },
   WARNING("Warnung", 3) {
      @Override
      public void accept(IMessageTypeVisitor visitor) {
         visitor.visitWarning(this);
      }
   },
   INFORMATION("Information", 2) {
      @Override
      public void accept(IMessageTypeVisitor visitor) {
         visitor.visitInformation(this);
      }
   },
   NORMAL("Normal", 1) {
      @Override
      public void accept(IMessageTypeVisitor visitor) {
         visitor.visitNormal(this);
      }
   },
   QUESTION("Question", 0) {
      @Override
      public void accept(IMessageTypeVisitor visitor) {
         visitor.visitQuestion(this);
      }
   };

   private final Integer priority;
   private String name;

   private MessageType(String name, Integer priority) {
      this.name = name;
      this.priority = priority;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public abstract void accept(IMessageTypeVisitor var1);

   public static MessageType[] getAll() {
      return values();
   }

   public Integer getPriority() {
      return this.priority;
   }
}
