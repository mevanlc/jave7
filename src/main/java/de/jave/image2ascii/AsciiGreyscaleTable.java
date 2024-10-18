package de.jave.image2ascii;

import net.disy.commons.core.util.Ensure;

public class AsciiGreyscaleTable {
   private final char[] greyscales;
   private final char[][] greyscales4;
   private final boolean[] defaultIgnores;
   private final boolean[] defaultIgnores4;
   private final boolean[] ignores;
   private final boolean[] ignores4;
   private char[] characters;
   private final char[][] characters4;

   public AsciiGreyscaleTable(boolean[] defaultIgnores, boolean[] defaultIgnores4, char[] greyscales, char[][] greyscales4, char[][] characters4) {
      Ensure.ensureArgumentTrue("Expected 95 greyscales, but was " + greyscales.length, greyscales.length == 95);
      Ensure.ensureArgumentTrue("Expected 95 greyscales4, but was " + greyscales4.length, greyscales4.length == 95);
      Ensure.ensureArgumentTrue("Expected at most 95 characters4, but was " + characters4.length, characters4.length <= 95);
      this.defaultIgnores = defaultIgnores;
      this.defaultIgnores4 = defaultIgnores4;
      this.greyscales = greyscales;
      this.greyscales4 = greyscales4;
      this.characters4 = characters4;
      this.ignores = new boolean[96];
      this.ignores4 = new boolean[96];
      System.arraycopy(defaultIgnores, 0, this.ignores, 0, 95);
      System.arraycopy(defaultIgnores4, 0, this.ignores4, 0, 95);
      this.buildCharactersForBrightness();
   }

   private void buildCharactersForBrightness() {
      this.characters = new char[256];

      for (int i = 0; i < 95; i++) {
         if (!this.ignores[i]) {
            this.characters[this.greyscales[i]] = (char)(32 + i);
         }
      }

      if (this.characters[255] == 0 || this.characters[0] == 0) {
         int darkestCharIndex = 0;
         int brightestCharIndex = 0;

         for (int ix = 0; ix < 95; ix++) {
            if (!this.ignores[ix]) {
               if (this.greyscales[ix] < this.greyscales[darkestCharIndex]) {
                  darkestCharIndex = ix;
               }

               if (this.greyscales[ix] > this.greyscales[brightestCharIndex]) {
                  brightestCharIndex = ix;
               }
            }
         }

         this.characters[255] = (char)(brightestCharIndex + 32);
         this.characters[0] = (char)(darkestCharIndex + 32);
      }

      int nextCharIndex = 0;

      for (int index = 0; index < 255; index++) {
         if (this.characters[index] == 0) {
            if (nextCharIndex <= index) {
               nextCharIndex = index + 1;

               while (this.characters[nextCharIndex] == 0) {
                  nextCharIndex++;
               }
            }

            int dLower = index - this.greyscales[this.characters[index - 1] - ' '];
            int dUpper = this.greyscales[this.characters[nextCharIndex] - ' '] - index;
            this.characters[index] = dLower < dUpper ? this.characters[index - 1] : this.characters[nextCharIndex];
         }
      }
   }

   public char getCharForBrightness(int i) {
      if (i > 255) {
         return this.characters[255];
      } else {
         return i < 0 ? this.characters[0] : this.characters[i];
      }
   }

   public int getBrightnessForChar(char ch) {
      if (ch < ' ') {
         return this.greyscales[0];
      } else {
         return ch > 126 ? this.greyscales[94] : this.greyscales[ch - 32];
      }
   }

   public char[] getBrightness4ForChar(char ch) {
      int index = ch - ' ';
      if (ch < ' ') {
         index = 0;
      }

      if (ch > '~') {
         index = 94;
      }

      return new char[]{this.greyscales4[index][0], this.greyscales4[index][1], this.greyscales4[index][2], this.greyscales4[index][3]};
   }

   public char getCharForBrightness(char[] brightness) {
      return this.getCharForBrightness(brightness[0], brightness[1], brightness[2], brightness[3]);
   }

   public char getCharForBrightness(int nw, int ne, int sw, int se) {
      return findNearestMaxQuick(nw, ne, sw, se, this.characters4, this.ignores4);
   }

   public char[][] getCharacters4() {
      return this.characters4;
   }

   private static final char findNearestMaxQuick(int nw, int ne, int sw, int se, char[][] table, boolean[] ignores4) {
      int length = (table.length + 1) / 2;
      if (nw <= 0 && ne <= 0 && sw <= 0 && se <= 0 && !ignores4[table[0][4] - ' ']) {
         return table[0][4];
      } else if (nw >= 255 && ne >= 255 && sw >= 255 && se >= 255 && !ignores4[table[table.length - 1][4] - ' ']) {
         return table[table.length - 1][4];
      } else {
         int bestIndex = length;
         boolean done = false;

         while (length > 1 && !done && bestIndex >= 0 && bestIndex < table.length) {
            length = (length + 1) / 2;
            if (table[bestIndex][0] < nw) {
               bestIndex += length;
            } else if (table[bestIndex][0] > nw) {
               bestIndex -= length;
            } else {
               done = true;
            }
         }

         if (bestIndex < 0) {
            bestIndex = 0;
         }

         if (bestIndex >= table.length) {
            bestIndex = table.length - 1;
         }

         if (ignores4[table[bestIndex][4] - ' ']) {
            int n = 1;

            while (true) {
               int d = (n + 1) / 2 * (2 * (n % 2) - 1);
               if (bestIndex + d < table.length && bestIndex + d >= 0 && !ignores4[table[bestIndex + d][4] - ' ']) {
                  bestIndex += d;
                  break;
               }

               n++;
            }
         }

         int d1 = table[bestIndex][0] - nw;
         d1 = d1 > 0 ? d1 : -d1;
         int d2 = table[bestIndex][1] - ne;
         d2 = d2 > 0 ? d2 : -d2;
         d1 = d1 > d2 ? d1 : d2;
         d2 = table[bestIndex][2] - sw;
         d2 = d2 > 0 ? d2 : -d2;
         d1 = d1 > d2 ? d1 : d2;
         d2 = table[bestIndex][3] - se;
         d2 = d2 > 0 ? d2 : -d2;
         d1 = d1 > d2 ? d1 : d2;
         int bestDistance = d1;
         int startIndex = bestIndex;
         int maxValue = nw + d1;

         for (int index = bestIndex + 1; index < table.length; index++) {
            d1 = table[index][0] - nw;
            d1 = d1 > 0 ? d1 : -d1;
            if (d1 > maxValue) {
               break;
            }

            d2 = table[index][1] - ne;
            d2 = d2 > 0 ? d2 : -d2;
            d1 = d1 > d2 ? d1 : d2;
            d2 = table[index][2] - sw;
            d2 = d2 > 0 ? d2 : -d2;
            d1 = d1 > d2 ? d1 : d2;
            d2 = table[index][3] - se;
            d2 = d2 > 0 ? d2 : -d2;
            d1 = d1 > d2 ? d1 : d2;
            if (d1 < bestDistance && !ignores4[table[index][4] - ' ']) {
               bestDistance = d1;
               bestIndex = index;
               maxValue = nw + d1;
            }
         }

         int minValue = nw - bestDistance;

         for (int var49 = startIndex - 1; var49 >= 0; var49--) {
            d1 = table[var49][0] - nw;
            d1 = d1 > 0 ? d1 : -d1;
            if (d1 < minValue) {
               break;
            }

            d2 = table[var49][1] - ne;
            d2 = d2 > 0 ? d2 : -d2;
            d1 = d1 > d2 ? d1 : d2;
            d2 = table[var49][2] - sw;
            d2 = d2 > 0 ? d2 : -d2;
            d1 = d1 > d2 ? d1 : d2;
            d2 = table[var49][3] - se;
            d2 = d2 > 0 ? d2 : -d2;
            d1 = d1 > d2 ? d1 : d2;
            if (d1 < bestDistance && !ignores4[table[var49][4] - ' ']) {
               bestDistance = d1;
               bestIndex = var49;
               minValue = nw - d1;
            }
         }

         return table[bestIndex][4];
      }
   }

   public void setDoNotUse(String doNotUse) {
      System.arraycopy(this.defaultIgnores, 0, this.ignores, 0, 95);
      if (doNotUse != null) {
         for (int i = doNotUse.length() - 1; i >= 0; i--) {
            int index = doNotUse.charAt(i) - ' ';
            if (index >= 0 && index <= 94) {
               this.ignores[index] = true;
            }
         }

         this.buildCharactersForBrightness();
      }
   }

   public void setDoNotUse4(String doNotUse) {
      System.arraycopy(this.defaultIgnores4, 0, this.ignores4, 0, 95);
      if (doNotUse != null) {
         for (int i = doNotUse.length() - 1; i >= 0; i--) {
            int index = doNotUse.charAt(i) - ' ';
            if (index >= 0 && index <= 94) {
               this.ignores4[index] = true;
            }
         }
      }
   }

   public void print() {
      for (int i = 0; i < 96; i++) {
         System.out
            .print(
               (char)(i + 32)
                  + ": "
                  + this.greyscales[i]
                  + " "
                  + this.greyscales4[i][0]
                  + " "
                  + this.greyscales4[i][1]
                  + " "
                  + this.greyscales4[i][2]
                  + " "
                  + this.greyscales4[i][3]
            );
         if (this.ignores[i]) {
            System.out.println(" <ignored>");
         } else {
            System.out.println();
         }
      }

      for (int ix = 0; ix < 256; ix++) {
         System.out.println(ix + ": " + this.characters[ix]);
      }

      for (int ix = 0; ix < 96; ix++) {
         System.out
            .println(
               this.characters4[ix][0]
                  + " "
                  + this.characters4[ix][1]
                  + " "
                  + this.characters4[ix][2]
                  + " "
                  + this.characters4[ix][3]
                  + " "
                  + this.characters4[ix][4]
            );
      }
   }

   public int getValueNW(int characterCode) {
      return this.greyscales4[characterCode - 32][0];
   }

   public int getValueNE(int characterCode) {
      return this.greyscales4[characterCode - 32][1];
   }

   public int getValueSW(int characterCode) {
      return this.greyscales4[characterCode - 32][2];
   }

   public int getValueSE(int characterCode) {
      return this.greyscales4[characterCode - 32][3];
   }
}
