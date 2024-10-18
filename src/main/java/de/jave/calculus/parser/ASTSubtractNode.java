package de.jave.calculus.parser;

public class ASTSubtractNode extends SimpleNode {
   public ASTSubtractNode(int id) {
      super(id);
   }

   public ASTSubtractNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
