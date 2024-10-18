package de.jave.calculus.parser;

public interface IFunction {
   double evaluate(double[] var1);

   String getName();

   int getArgumentCount();
}
