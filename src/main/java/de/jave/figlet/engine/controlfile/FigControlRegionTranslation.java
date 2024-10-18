package de.jave.figlet.engine.controlfile;

import de.jave.figlet.engine.IFigConversionContext;

public class FigControlRegionTranslation extends FigControlRule {
   private final int fromCharStart;
   private final int fromCharEnd;
   private final int toCharStart;

   public FigControlRegionTranslation(String line, IFigConversionContext context) {
      int i1 = line.indexOf(32);
      int i2 = line.indexOf(32, i1 + 1);
      if (i1 != -1 && i2 != -1) {
         String s1 = line.substring(i1 + 1, i2);
         String s2 = line.substring(i2 + 1);
         if (s1.startsWith("\\-")) {
            i1 = s1.indexOf(45, 2);
         } else {
            i1 = s1.indexOf(45);
         }

         String s1a = s1.substring(0, i1);
         String s1b = s1.substring(i1 + 1);
         if (s2.startsWith("\\-")) {
            i1 = s2.indexOf(45, 2);
         } else {
            i1 = s2.indexOf(45);
         }

         String s2a = s2.substring(0, i1);
         this.fromCharStart = this.convertToCharCode(s1a, context);
         this.fromCharEnd = this.convertToCharCode(s1b, context);
         this.toCharStart = this.convertToCharCode(s2a, context);
      } else {
         context.addWarning(line + " is no correct rule!");
         this.fromCharStart = -1;
         this.fromCharEnd = -1;
         this.toCharStart = -1;
      }
   }

   @Override
   public int map(int charIn, IFigConversionContext context) {
      return charIn >= this.fromCharStart && charIn <= this.fromCharEnd ? charIn - this.fromCharStart + this.toCharStart : -1;
   }

   @Override
   public String toString() {
      return "mapping " + this.fromCharStart + "-" + this.fromCharEnd + " to " + this.toCharStart;
   }
}
