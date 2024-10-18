package de.jave.calculus.parser;

public interface CalculusVisitor {
   Object visit(SimpleNode var1, Object var2);

   Object visit(ASTStart var1, Object var2);

   Object visit(ASTAddNode var1, Object var2);

   Object visit(ASTSubtractNode var1, Object var2);

   Object visit(ASTMultNode var1, Object var2);

   Object visit(ASTDivNode var1, Object var2);

   Object visit(ASTModNode var1, Object var2);

   Object visit(ASTExpNode var1, Object var2);

   Object visit(ASTNegativeNode var1, Object var2);

   Object visit(ASTXNode var1, Object var2);

   Object visit(ASTConstNode var1, Object var2);

   Object visit(ASTFunctionNode var1, Object var2);

   Object visit(ASTInteger var1, Object var2);

   Object visit(ASTFloat var1, Object var2);
}
