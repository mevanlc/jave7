package de.jave.calculus.parser;

public class ASTExpNode extends SimpleNode {
   public ASTExpNode(int id) {
      super(id);
   }

   public ASTExpNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
