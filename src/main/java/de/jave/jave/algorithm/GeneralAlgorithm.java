package de.jave.jave.algorithm;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GeneralAlgorithm {
   public static final int replace(CharacterPlate plate, char ch1, char ch2) {
      int count = 0;
      int h = plate.getHeight();
      int w = plate.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            char c = plate.get(x, y);
            if (c == ch1) {
               plate.setForce(x, y, ch2);
               count++;
            }
         }
      }

      return count;
   }

   public static final int replace(CharacterPlate plate, CharacterPlate cp1, CharacterPlate cp2, boolean wildcard, char wildcardChar) {
      int count = 0;
      int height = plate.getHeight();
      int width = plate.getWidth();
      int patternWidth = cp1.getWidth();
      int patternHeight = cp2.getHeight();
      List<Point> fits = new ArrayList<>();

      for (int y = 0; y <= height - patternHeight; y++) {
         for (int x = 0; x <= width - patternWidth; x++) {
            if (replaceFits(plate, cp1, x, y, wildcard, wildcardChar)) {
               fits.add(new Point(x, y));
               count++;
            }
         }
      }

      if (count > 0) {
         for (int i = 0; i < count; i++) {
            Point p = fits.get(i);
            replaceApply(plate, cp2, p.x, p.y, wildcard, wildcardChar);
         }
      }

      return count;
   }

   private static final boolean replaceFits(CharacterPlate plate, CharacterPlate pattern, int x0, int y0, boolean wildcard, char wildcardChar) {
      int h = pattern.getHeight();
      int w = pattern.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if ((!wildcard || pattern.get(x, y) != wildcardChar) && pattern.get(x, y) != plate.get(x0 + x, y0 + y)) {
               return false;
            }
         }
      }

      return true;
   }

   private static final void replaceApply(CharacterPlate plate, CharacterPlate pattern, int x0, int y0, boolean wildcard, char wildcardChar) {
      int h = pattern.getHeight();
      int w = pattern.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (plate.contains(x0 + x, y0 + y) && (!wildcard || wildcardChar != pattern.get(x, y))) {
               plate.setForce(x0 + x, y0 + y, pattern.get(x, y));
            }
         }
      }
   }
}
