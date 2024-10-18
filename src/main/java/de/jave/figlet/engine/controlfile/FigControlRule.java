package de.jave.figlet.engine.controlfile;

import de.jave.figlet.engine.IFigConversionContext;
import de.jave.figlet.util.FigUtilities;

public abstract class FigControlRule {
   public abstract int map(int var1, IFigConversionContext var2);

   protected int convertToCharCode(String string, IFigConversionContext context) {
      int result = toCharCode(string);
      if (result == -1) {
         context.addWarning("In figlet control file: " + string + " is no correct character!");
      }

      return result;
   }

   private static int toCharCode(String s) {
      if (s.length() == 1) {
         return s.charAt(0);
      } else if (s.charAt(0) == '\\') {
         s = s.substring(1);
         switch (s.charAt(0)) {
            case ' ':
               return 32;
            case '\\':
               return 92;
            case 'a':
               return 7;
            case 'b':
               return 8;
            case 'e':
               return 27;
            case 'f':
               return 12;
            case 'n':
               return 10;
            case 'r':
               return 13;
            case 't':
               return 9;
            case 'v':
               return 11;
            default:
               return FigUtilities.stringToInt(s);
         }
      } else {
         return -1;
      }
   }

   public void dump() {
      System.out.println(this);
   }
}
