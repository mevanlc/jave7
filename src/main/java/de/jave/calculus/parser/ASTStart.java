package de.jave.calculus.parser;

public class ASTStart extends SimpleNode {
   public ASTStart(int id) {
      super(id);
   }

   public ASTStart(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
