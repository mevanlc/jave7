package de.jave.calculus.parser;

public class ASTNegativeNode extends SimpleNode {
   public ASTNegativeNode(int id) {
      super(id);
   }

   public ASTNegativeNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
