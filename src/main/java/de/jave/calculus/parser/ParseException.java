package de.jave.calculus.parser;

public class ParseException extends Exception {
   private static final long serialVersionUID = 1L;
   public Token currentToken;
   public int[][] expectedTokenSequences;
   public String[] tokenImage;
   protected String eol = System.getProperty("line.separator", "\n");

   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
      super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
      this.currentToken = currentTokenVal;
      this.expectedTokenSequences = expectedTokenSequencesVal;
      this.tokenImage = tokenImageVal;
   }

   public ParseException() {
   }

   public ParseException(String message) {
      super(message);
   }

   private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage) {
      String eol = System.getProperty("line.separator", "\n");
      StringBuffer expected = new StringBuffer();
      int maxSize = 0;

      for (int i = 0; i < expectedTokenSequences.length; i++) {
         if (maxSize < expectedTokenSequences[i].length) {
            maxSize = expectedTokenSequences[i].length;
         }

         for (int j = 0; j < expectedTokenSequences[i].length; j++) {
            expected.append(tokenImage[expectedTokenSequences[i][j]]).append(' ');
         }

         if (expectedTokenSequences[i][expectedTokenSequences[i].length - 1] != 0) {
            expected.append("...");
         }

         expected.append(eol).append("    ");
      }

      String retval;
      Token tok = currentToken.next;

       StringBuilder retvalBuilder = new StringBuilder("Encountered \"");
       for (int i = 0; i < maxSize; i++) {
         if (i != 0) {
            retvalBuilder.append(" ");
         }

         if (tok.kind == 0) {
            retvalBuilder.append(tokenImage[0]);
            break;
         }

         retvalBuilder.append(" ").append(tokenImage[tok.kind]);
         retvalBuilder.append(" \"");
         retvalBuilder.append(add_escapes(tok.image));
         retvalBuilder.append(" \"");
         tok = tok.next;
      }
       retval = retvalBuilder.toString();

       retval = retval + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
      retval = retval + "." + eol;
      if (expectedTokenSequences.length == 1) {
         retval = retval + "Was expecting:" + eol + "    ";
      } else {
         retval = retval + "Was expecting one of:" + eol + "    ";
      }

      return retval + expected.toString();
   }

   static String add_escapes(String str) {
      StringBuffer retval = new StringBuffer();

      for (int i = 0; i < str.length(); i++) {
         switch (str.charAt(i)) {
            case '\u0000':
               break;
            case '\b':
               retval.append("\\b");
               break;
            case '\t':
               retval.append("\\t");
               break;
            case '\n':
               retval.append("\\n");
               break;
            case '\f':
               retval.append("\\f");
               break;
            case '\r':
               retval.append("\\r");
               break;
            case '"':
               retval.append("\\\"");
               break;
            case '\'':
               retval.append("\\'");
               break;
            case '\\':
               retval.append("\\\\");
               break;
            default:
               char ch;
               if ((ch = str.charAt(i)) >= ' ' && ch <= '~') {
                  retval.append(ch);
               } else {
                  String s = "0000" + Integer.toString(ch, 16);
                  retval.append("\\u").append(s.substring(s.length() - 4));
               }
         }
      }

      return retval.toString();
   }
}
