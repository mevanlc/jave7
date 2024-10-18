package de.jave.calculus.parser;

public interface CalculusTreeConstants {
   int JJTSTART = 0;
   int JJTVOID = 1;
   int JJTADDNODE = 2;
   int JJTSUBTRACTNODE = 3;
   int JJTMULTNODE = 4;
   int JJTDIVNODE = 5;
   int JJTMODNODE = 6;
   int JJTEXPNODE = 7;
   int JJTNEGATIVENODE = 8;
   int JJTXNODE = 9;
   int JJTCONSTNODE = 10;
   int JJTFUNCTIONNODE = 11;
   int JJTINTEGER = 12;
   int JJTFLOAT = 13;
   String[] jjtNodeName = new String[]{
      "Start",
      "void",
      "AddNode",
      "SubtractNode",
      "MultNode",
      "DivNode",
      "ModNode",
      "ExpNode",
      "NegativeNode",
      "XNode",
      "ConstNode",
      "FunctionNode",
      "Integer",
      "Float"
   };
}
