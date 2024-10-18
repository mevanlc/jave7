package de.jave.jave.algorithm.fill;

import de.jave.lib.CharacterPlate;
import java.awt.Rectangle;
import java.util.Stack;
import net.disy.commons.core.util.Ensure;

public class FillMatcher {
   public static final char MARK = '\u0002';
   private final FillMatchMode matchMode;
   private final CharacterPlate plate;

   public FillMatcher(FillMatchMode matchMode, CharacterPlate plate) {
      Ensure.ensureArgumentNotNull(matchMode);
      Ensure.ensureArgumentNotNull(plate);
      this.matchMode = matchMode;
      this.plate = plate;
   }

   private boolean matches(int x, int y, char previousChar) {
      if (x >= 0 && y >= 0 && x < this.plate.getWidth() && y < this.plate.getHeight()) {
         if (this.matchMode == FillMatchMode.EQUAL_CHARACTER && this.plate.get(x, y) != previousChar) {
            return false;
         } else {
            if (this.matchMode != FillMatchMode.EQUAL_CHARACTER) {
               char c = this.plate.get(x, y);
               if (c == ' ' || c == 2) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public Rectangle markAreaForFill(int x0, int y0) {
      char previousChar = this.plate.get(x0, y0);
      if (!this.matches(x0, y0, previousChar)) {
         return null;
      } else {
         Bounds bounds = new Bounds(x0, y0);
         int delta = 0;
         if (this.matchMode == FillMatchMode.ANY_CHARACTER_DIAGONAL) {
            delta = 1;
         }

         Stack<FillMatcher.Span> spans = new Stack<>();
         spans.add(new FillMatcher.Span(x0, x0, y0));

         while (spans.size() > 0) {
            FillMatcher.Span span = spans.pop();
            int start = span.getMinX();
            int end = span.getMaxX();
            int y = span.getY();

            while (start > 0 && this.matches(start - 1, y, previousChar)) {
               start--;
            }

            while (end < this.plate.getWidth() - 1 && this.matches(end + 1, y, previousChar)) {
               end++;
            }

            for (int i = start; i <= end; i++) {
               this.plate.setForce(i, y, '\u0002');
            }

            bounds.add(start, y);
            bounds.add(end, y);
            if (y > 0) {
               int s1 = -1;
               int s2 = -1;

               for (int x = start - delta; x <= end + delta; x++) {
                  if (this.matches(x, y - 1, previousChar)) {
                     if (s1 == -1) {
                        s1 = x;
                        s2 = x;
                     } else {
                        s2++;
                     }
                  } else {
                     if (s1 != -1) {
                        spans.add(new FillMatcher.Span(s1, s2, y - 1));
                     }

                     s1 = -1;
                     s2 = -1;
                  }
               }

               if (s1 != -1) {
                  spans.add(new FillMatcher.Span(s1, s2, y - 1));
               }
            }

            if (y < this.plate.getHeight() - 1) {
               int s1 = -1;
               int s2 = -1;

               for (int xx = start - delta; xx <= end + delta; xx++) {
                  if (this.matches(xx, y + 1, previousChar)) {
                     if (s1 == -1) {
                        s1 = xx;
                        s2 = xx;
                     } else {
                        s2++;
                     }
                  } else {
                     if (s1 != -1) {
                        spans.add(new FillMatcher.Span(s1, s2, y + 1));
                     }

                     s1 = -1;
                     s2 = -1;
                  }
               }

               if (s1 != -1) {
                  spans.add(new FillMatcher.Span(s1, s2, y + 1));
               }
            }
         }

         return bounds.getRectangle();
      }
   }

   private static final class Span {
      private final int minX;
      private final int maxX;
      private final int y;

      public Span(int minX, int maxX, int y) {
         this.minX = minX;
         this.maxX = maxX;
         this.y = y;
      }

      public int getMinX() {
         return this.minX;
      }

      public int getMaxX() {
         return this.maxX;
      }

      public int getY() {
         return this.y;
      }
   }
}
