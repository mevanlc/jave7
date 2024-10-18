package de.jave.calculus.parser;

public class ASTInteger extends SimpleNode {
   private int value;

   public ASTInteger(int id) {
      super(id);
   }

   public ASTInteger(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public void setValue(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
