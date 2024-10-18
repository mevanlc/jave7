package de.jave.calculus.parser;

public class ASTXNode extends SimpleNode {
   public ASTXNode(int id) {
      super(id);
   }

   public ASTXNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
