package de.jave.jave.algorithm.repair;

import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;
import net.dizzy.commons.core.util.Ensure;

public class AsciiRepairAlgorithm {
   private final AsciiRepairAlgorithmConfiguration configuration;

   public AsciiRepairAlgorithm(AsciiRepairAlgorithmConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   public CharacterPlate repairShaked(CharacterPlate plate) {
      int h = plate.getHeight();
      int maxLength = 0;
      String[] lines = new String[h];

      for (int y = 0; y < h; y++) {
         lines[y] = plate.getLine(y).trim();
         if (lines[y].length() > maxLength) {
            maxLength = lines[y].length();
         }
      }

      if (maxLength == 0) {
         return null;
      } else {
         for (int yx = 0; yx < h - 1; yx++) {
            String a = lines[yx];
            String b = lines[yx + 1];
            if (b.length() != 0) {
               int maxV = 0;
               int maxRating = 0;

               for (int v = -b.length(); v <= a.length(); v++) {
                  int rating = this.rate(a, b, v);
                  if (rating > maxRating) {
                     maxRating = rating;
                     maxV = v;
                  }
               }

               if (maxV >= 0) {
                  if (maxV > 0) {
                     while (maxV > 0) {
                        lines[yx + 1] = " " + lines[yx + 1];
                        maxV--;
                     }
                  }
               } else {
                  String s;
                  for (s = ""; maxV < 0; maxV++) {
                     s = s + " ";
                  }

                  for (int i = 0; i <= yx; i++) {
                     lines[i] = s + lines[i];
                  }
               }
            }
         }

         return new CharacterPlate(lines);
      }
   }

   private int rate(String a, String b, int v) {
      int length = 0;
      if (v > 0) {
         length = Toolbox.max(a.length(), b.length() + v);
      } else {
         length = Toolbox.max(a.length(), b.length() + v) - v;
      }

      char[] ch1 = new char[length];
      char[] ch2 = new char[length];

      for (int x = 0; x < length; x++) {
         ch1[x] = ' ';
         ch2[x] = ' ';
      }

      char[] chA = a.toCharArray();
      char[] chB = b.toCharArray();
      if (v < 0) {
         System.arraycopy(chA, 0, ch1, -v, chA.length);
         System.arraycopy(chB, 0, ch2, 0, chB.length);
      } else {
         System.arraycopy(chA, 0, ch1, 0, chA.length);
         System.arraycopy(chB, 0, ch2, v, chB.length);
      }

      return this.rate(ch1, ch2);
   }

   private int rate(char[] a, char[] b) {
      int identical = this.configuration.getIdentical();
      int identicalLeft = this.configuration.getIdenticalLeft();
      int identicalRight = this.configuration.getIdenticalRight();
      AsciiRepairRule[] rules = this.configuration.getRules();
      int result = 0;

      for (int x = 0; x < a.length; x++) {
         if (a[x] != ' ' && a[x] == b[x]) {
            result += identical;
         }
      }

      for (int xx = 1; xx < a.length; xx++) {
         if (a[xx] != ' ' && a[xx] == b[xx - 1]) {
            result += identicalLeft;
         }
      }

      for (int xxx = 0; xxx < a.length - 1; xxx++) {
         if (a[xxx] != ' ' && a[xxx] == b[xxx + 1]) {
            result += identicalRight;
         }
      }

      for (int i = 0; i < rules.length; i++) {
         result += rules[i].rate(a, b);
      }

      return result;
   }
}
