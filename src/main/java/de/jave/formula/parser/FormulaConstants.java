package de.jave.formula.parser;

public interface FormulaConstants {
   int EOF = 0;
   int EOL = 3;
   int PLUS = 4;
   int MINUS = 5;
   int DOT_DOT = 6;
   int MULTIPLY = 7;
   int DIVIDE = 8;
   int SUB = 9;
   int SUP = 10;
   int COMPARATOR = 11;
   int LESS_EQUAL = 12;
   int GREATER_EQUAL = 13;
   int IMPLIES = 14;
   int IMPLIES2 = 15;
   int UNEQUAL1 = 16;
   int UNEQUAL2 = 17;
   int UNEQUAL3 = 18;
   int FORALL = 19;
   int AND = 20;
   int OR = 21;
   int NOT = 22;
   int EXISTS = 23;
   int EMPTY_SPACE = 24;
   int SQRT_START = 25;
   int SUM_START = 26;
   int PROD_START = 27;
   int INTEGRAL_START = 28;
   int LIM_START = 29;
   int VEC_START = 30;
   int LOG_START = 31;
   int MATRIX_START = 32;
   int BINOM_START = 33;
   int FLOOR_START = 34;
   int CEIL_START = 35;
   int OVERLINE_START = 36;
   int UNDERLINE_START = 37;
   int STRING = 38;
   int DECIMAL_LITERAL = 39;
   int IDENTIFIER = 40;
   int LETTER = 41;
   int DIGIT = 42;
   int NUMBER = 43;
   int HEX_LITERAL = 44;
   int OCTAL_LITERAL = 45;
   int FLOATING_POINT_LITERAL = 46;
   int EXPONENT = 47;
   int DEFAULT = 0;
   String[] tokenImage = new String[]{
      "<EOF>",
      "\"\\t\"",
      "\"\\r\"",
      "\"\\n\"",
      "\"+\"",
      "\"-\"",
      "\"..\"",
      "\"*\"",
      "\"/\"",
      "\"_\"",
      "\"^\"",
      "<COMPARATOR>",
      "\"<=\"",
      "\">=\"",
      "\"=>\"",
      "\"->\"",
      "\"!=\"",
      "\"<>\"",
      "\"><\"",
      "<FORALL>",
      "\"AND\"",
      "\"OR\"",
      "\"NOT\"",
      "\"EXISTS\"",
      "<EMPTY_SPACE>",
      "<SQRT_START>",
      "\"sum\"",
      "<PROD_START>",
      "<INTEGRAL_START>",
      "<LIM_START>",
      "<VEC_START>",
      "<LOG_START>",
      "\"matrix\"",
      "\"binomial\"",
      "\"floor\"",
      "\"ceil\"",
      "\"overline\"",
      "\"underline\"",
      "<STRING>",
      "<DECIMAL_LITERAL>",
      "<IDENTIFIER>",
      "<LETTER>",
      "<DIGIT>",
      "<NUMBER>",
      "<HEX_LITERAL>",
      "<OCTAL_LITERAL>",
      "<FLOATING_POINT_LITERAL>",
      "<EXPONENT>",
      "\",\"",
      "\"|\"",
      "\";\"",
      "\"=\"",
      "\"<\"",
      "\">\"",
      "\"!\"",
      "\"(\"",
      "\")\"",
      "\"[\"",
      "\"]\"",
      "\"||\"",
      "\"{\"",
      "\"}\""
   };
}
