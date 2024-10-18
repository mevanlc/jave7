package de.jave.calculus.parser;

public abstract class Function implements IFunction {
   private final String name;
   private final int argumentCount;

   public Function(String name, int argumentCount) {
      this.argumentCount = argumentCount;
      this.name = name;
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public int getArgumentCount() {
      return this.argumentCount;
   }
}
