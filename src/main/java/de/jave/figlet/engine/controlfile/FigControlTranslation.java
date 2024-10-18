package de.jave.figlet.engine.controlfile;

import de.jave.figlet.engine.IFigConversionContext;

public class FigControlTranslation extends FigControlRule {
   private final int fromChar;
   private final int toChar;

   public FigControlTranslation(String line, IFigConversionContext context) {
      int i1 = line.indexOf(32);
      int i2 = line.indexOf(32, i1 + 1);
      if (i1 != -1 && i2 != -1) {
         String s1 = line.substring(i1 + 1, i2);
         String s2 = line.substring(i2 + 1);
         this.fromChar = this.convertToCharCode(s1, context);
         this.toChar = this.convertToCharCode(s2, context);
      } else {
         context.addWarning(line + " is no correct rule!");
         this.fromChar = -1;
         this.toChar = -1;
      }
   }

   @Override
   public int map(int charIn, IFigConversionContext context) {
      return charIn == this.fromChar ? this.toChar : -1;
   }

   @Override
   public String toString() {
      return "mapping " + this.fromChar + " to " + this.toChar;
   }
}
