package de.jave.calculus.parser;

public class FunctionParser {
   private static final IFunction[] FUNCTIONS = new IFunction[]{new Function("sin", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.sin(arguments[0]);
      }
   }, new Function("cos", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.cos(arguments[0]);
      }
   }, new Function("tan", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.tan(arguments[0]);
      }
   }, new Function("log", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.log(arguments[0]);
      }
   }, new Function("min", 2) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.min(arguments[0], arguments[1]);
      }
   }, new Function("max", 2) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.max(arguments[0], arguments[1]);
      }
   }, new Function("abs", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.abs(arguments[0]);
      }
   }, new Function("floor", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.floor(arguments[0]);
      }
   }, new Function("ceil", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.ceil(arguments[0]);
      }
   }, new Function("sqr", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.sqrt(arguments[0]);
      }
   }, new Function("sqrt", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.sqrt(arguments[0]);
      }
   }, new Function("sgn", 1) {
      @Override
      public double evaluate(double[] arguments) {
         return Math.signum(arguments[0]);
      }
   }};

   public static IFunction parseFunctionName(String name) {
      for (int i = 0; i < FUNCTIONS.length; i++) {
         if (name.equalsIgnoreCase(FUNCTIONS[i].getName())) {
            return FUNCTIONS[i];
         }
      }

      return null;
   }
}
