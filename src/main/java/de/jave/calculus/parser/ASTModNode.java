package de.jave.calculus.parser;

public class ASTModNode extends SimpleNode {
   public ASTModNode(int id) {
      super(id);
   }

   public ASTModNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
