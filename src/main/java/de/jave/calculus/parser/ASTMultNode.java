package de.jave.calculus.parser;

public class ASTMultNode extends SimpleNode {
   public ASTMultNode(int id) {
      super(id);
   }

   public ASTMultNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
