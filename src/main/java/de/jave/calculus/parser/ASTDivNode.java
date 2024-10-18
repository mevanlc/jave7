package de.jave.calculus.parser;

public class ASTDivNode extends SimpleNode {
   public ASTDivNode(int id) {
      super(id);
   }

   public ASTDivNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
