package de.jave.jave.algorithm.replaceillegal;

public class AsciiReplaceIllegalReport {
   private final int illegalCharacterCount;
   private final int replacedCharacterCount;

   public AsciiReplaceIllegalReport(int illegalCharacterCount, int replacedCharacterCount) {
      this.illegalCharacterCount = illegalCharacterCount;
      this.replacedCharacterCount = replacedCharacterCount;
   }

   public int getIllegalCharacterCount() {
      return this.illegalCharacterCount;
   }

   public int getReplacedCharacterCount() {
      return this.replacedCharacterCount;
   }
}
