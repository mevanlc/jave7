package net.disy.commons.core.message;

public interface IMessageTypeVisitor {
   void visitError(MessageType var1);

   void visitNormal(MessageType var1);

   void visitWarning(MessageType var1);

   void visitInformation(MessageType var1);

   void visitQuestion(MessageType var1);
}
