package de.jave.calculus.parser;

public class ASTAddNode extends SimpleNode {
   public ASTAddNode(int id) {
      super(id);
   }

   public ASTAddNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
