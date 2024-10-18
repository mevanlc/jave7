package de.jave.calculus.parser;

public class ASTConstNode extends SimpleNode {
   private double value;

   public ASTConstNode(int id) {
      super(id);
   }

   public ASTConstNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public void setValue(double value) {
      this.value = value;
   }

   public double getValue() {
      return this.value;
   }
}
