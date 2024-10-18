package de.jave.calculus.parser;

public class ASTFunctionNode extends SimpleNode {
   private IFunction function;

   public ASTFunctionNode(int id) {
      super(id);
   }

   public ASTFunctionNode(Calculus p, int id) {
      super(p, id);
   }

   @Override
   public Object jjtAccept(CalculusVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public void setFunctionName(String name) throws ParseException {
      this.function = FunctionParser.parseFunctionName(name);
      if (this.function == null) {
         throw new ParseException("No such function: '" + name + "'");
      }
   }

   public IFunction getFunction() {
      return this.function;
   }
}
