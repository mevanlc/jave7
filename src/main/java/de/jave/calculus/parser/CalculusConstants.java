package de.jave.calculus.parser;

public interface CalculusConstants {
   int EOF = 0;
   int PLUS = 7;
   int MINUS = 8;
   int MULTIPLY = 9;
   int DIVIDE = 10;
   int EXP = 11;
   int X = 12;
   int PI = 13;
   int E = 14;
   int INTEGER_LITERAL = 15;
   int DECIMAL_LITERAL = 16;
   int HEX_LITERAL = 17;
   int OCTAL_LITERAL = 18;
   int FLOAT_LITERAL = 19;
   int FLOAT = 20;
   int INTEGER = 21;
   int DIGIT = 22;
   int IDENTIFIER = 23;
   int LETTER = 24;
   int DEFAULT = 0;
   String[] tokenImage = new String[]{
      "<EOF>",
      "\" \"",
      "\"\\t\"",
      "\"\\n\"",
      "\"\\r\"",
      "<token of kind 5>",
      "<token of kind 6>",
      "\"+\"",
      "\"-\"",
      "\"*\"",
      "\"/\"",
      "\"^\"",
      "\"x\"",
      "<PI>",
      "<E>",
      "<INTEGER_LITERAL>",
      "<DECIMAL_LITERAL>",
      "<HEX_LITERAL>",
      "<OCTAL_LITERAL>",
      "<FLOAT_LITERAL>",
      "<FLOAT>",
      "<INTEGER>",
      "<DIGIT>",
      "<IDENTIFIER>",
      "<LETTER>",
      "\";\"",
      "\"%\"",
      "\"(\"",
      "\")\"",
      "\",\""
   };
}
