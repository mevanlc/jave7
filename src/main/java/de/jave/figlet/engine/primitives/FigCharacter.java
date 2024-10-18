package de.jave.figlet.engine.primitives;

import de.jave.text.QuickString;

public class FigCharacter {
   private final QuickString[] lines;
   private final int characterCode;

   public FigCharacter(int characterCode, String[] lines) {
      this.characterCode = characterCode;
      this.lines = new QuickString[lines.length];

      for (int i = 0; i < lines.length; i++) {
         if (lines[i] == null) {
            this.lines[i] = new QuickString();
         } else {
            this.lines[i] = new QuickString(lines[i]);
         }
      }
   }

   public QuickString getLine(int lineIndex) {
      return this.lines[lineIndex];
   }

   public int getHeight() {
      return this.lines.length;
   }

   public void dump() {
      System.out.println(this);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();

      for (int lineIndex = 0; lineIndex < this.getHeight(); lineIndex++) {
         if (lineIndex > 0) {
            sb.append('\n');
         }

         sb.append(this.getLine(lineIndex));
      }

      return sb.toString();
   }

   public int getCharacterCode() {
      return this.characterCode;
   }

   public int getWidth() {
      int width = 0;

      for (int i = 0; i < this.lines.length; i++) {
         int lineWidth = this.getRawWidth(this.lines[i]);
         if (lineWidth > width) {
            width = lineWidth;
         }
      }

      return width;
   }

   private int getRawWidth(QuickString string) {
      int width = string.length();

      for (int index = 0; index < string.length() && string.charAt(index) == 127; width--) {
         index++;
      }

      for (int var4 = string.length() - 1; width > 0 && var4 >= 0 && string.charAt(var4) == 127; width--) {
         var4--;
      }

      return width;
   }
}
