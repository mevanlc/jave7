package de.jave.jave.algorithm.steganogram;

import de.jave.jave.Ascii;
import de.jave.lib.CharacterPlate;

public class GradientSteganogramDecoder {
   public static String decode(CharacterPlate plate, String gradient) {
      StringBuffer result = new StringBuffer();
      int x = 0;
      int y = 0;

      do {
         char lastChar = 0;

         for (int i = 0; i < 8; i++) {
            boolean bit = gradient.indexOf(plate.glyphAt(x, y)) % 2 == 1;
            if (bit) {
               lastChar = (char)(lastChar | 1 << i);
            }

            if (++x >= plate.getWidth()) {
               x = 0;
               y++;
            }
         }

         if (Ascii.isAscii(lastChar)) {
            result.append(lastChar);
         } else {
            y = plate.getHeight();
         }
      } while (y < plate.getHeight());

      return result.toString();
   }
}
