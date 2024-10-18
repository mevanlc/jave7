package de.jave.calculus.parser;

public interface Node {
   void jjtOpen();

   void jjtClose();

   void jjtSetParent(Node var1);

   Node jjtGetParent();

   void jjtAddChild(Node var1, int var2);

   Node jjtGetChild(int var1);

   int jjtGetNumChildren();

   Object jjtAccept(CalculusVisitor var1, Object var2);
}
