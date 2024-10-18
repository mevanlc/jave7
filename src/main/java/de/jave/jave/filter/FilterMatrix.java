package de.jave.jave.filter;

import de.jave.jave.RectangleAlgorithm;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.lib.CharacterPlate;
import java.awt.Insets;
import java.awt.Rectangle;

public class FilterMatrix {
   private final char[][] mask;
   private final int maskWidth;
   private final int maskHeight;
   private final int maskCenterX;
   private final int maskCenterY;
   private final char[][] result;
   private final int resultWidth;
   private final int resultHeight;
   private final int resultCenterX;
   private final int resultCenterY;
   private int minimumNearest9CharacterCount;
   private int optionalNearest9CharacterCount;

   public FilterMatrix(CharacterPlate mask, CharacterPlate result) {
      int originalMaskWidth = mask.getWidth();
      int originalMaskHeight = mask.getHeight();
      Insets maskInsets = mask.getEmptyInsets('?');
      this.maskWidth = originalMaskWidth - maskInsets.left - maskInsets.right;
      this.maskHeight = originalMaskHeight - maskInsets.top - maskInsets.bottom;
      this.maskCenterX = (originalMaskWidth - 1) / 2 - maskInsets.left;
      this.maskCenterY = (originalMaskHeight - 1) / 2 - maskInsets.top;
      this.mask = mask.getCopy(maskInsets.left, maskInsets.top, this.maskWidth, this.maskHeight).getContent();
      int originalResultWidth = result.getWidth();
      int originalResultHeight = result.getHeight();
      Insets resultInsets = result.getEmptyInsets('?');
      this.resultWidth = originalResultWidth - resultInsets.left - resultInsets.right;
      this.resultHeight = originalResultHeight - resultInsets.top - resultInsets.bottom;
      this.resultCenterX = (originalResultWidth - 1) / 2 - resultInsets.left;
      this.resultCenterY = (originalResultHeight - 1) / 2 - resultInsets.top;
      this.result = result.getCopy(resultInsets.left, resultInsets.top, this.resultWidth, this.resultHeight).getContent();
      this.optionalNearest9CharacterCount = 0;
      this.minimumNearest9CharacterCount = 0;

      for (int y = this.maskCenterY - 1; y <= this.maskCenterY + 1; y++) {
         for (int x = this.maskCenterX - 1; x <= this.maskCenterX + 1; x++) {
            if (y >= 0 && y < this.maskHeight && x >= 0 && x < this.maskWidth && this.mask[y][x] != ' ') {
               if (this.mask[y][x] == '?') {
                  this.optionalNearest9CharacterCount++;
               } else {
                  this.minimumNearest9CharacterCount++;
               }
            }
         }
      }
   }

   public boolean fits(CharacterPlate plate, int plateWidth, int plateHeight, int x, int y, int charactersSetInNear9) {
      if ((
            charactersSetInNear9 < this.minimumNearest9CharacterCount
               || charactersSetInNear9 > this.minimumNearest9CharacterCount + this.optionalNearest9CharacterCount
         )
         && charactersSetInNear9 > -1) {
         return false;
      } else {
         int minPlateX = x - this.maskCenterX;
         if (minPlateX < 0) {
            return false;
         } else {
            int maxPlateX = x - this.maskCenterX + this.maskWidth - 1;
            if (maxPlateX >= plateWidth) {
               return false;
            } else {
               int minPlateY = y - this.maskCenterY;
               if (minPlateY < 0) {
                  return false;
               } else {
                  int maxPlateY = y - this.maskCenterY + this.maskHeight - 1;
                  if (maxPlateY >= plateHeight) {
                     return false;
                  } else {
                     for (int plateX = minPlateX; plateX <= maxPlateX; plateX++) {
                        for (int plateY = minPlateY; plateY <= maxPlateY; plateY++) {
                           int maskX = plateX - x + this.maskCenterX;
                           int maskY = plateY - y + this.maskCenterY;
                           char maskCharacter = this.mask[maskY][maskX];
                           if (maskCharacter != '?') {
                              char plateCharacter = plate.get(plateX, plateY);
                              if ((maskCharacter != '!' || plateCharacter == ' ') && plateCharacter != maskCharacter) {
                                 return false;
                              }
                           }
                        }
                     }

                     return true;
                  }
               }
            }
         }
      }
   }

   public void apply(CharacterPlate cp, int w, int h, int x, int y) {
      int x0 = x - this.resultCenterX;
      if (x0 < 0) {
         x0 = 0;
      }

      int x1 = x - this.resultCenterX + this.resultWidth - 1;
      if (x1 >= w) {
         x1 = w - 1;
      }

      int y0 = y - this.resultCenterY;
      if (y0 < 0) {
         y0 = 0;
      }

      int y1 = y - this.resultCenterY + this.resultHeight - 1;
      if (y1 >= h) {
         y1 = h - 1;
      }

      for (int i = x0; i <= x1; i++) {
         for (int j = y0; j <= y1; j++) {
            char ch = this.result[j - y + this.resultCenterY][i - x + this.resultCenterX];
            if (ch != '?') {
               cp.setForce(i, j, ch);
            }
         }
      }
   }

   @Override
   public String toString() {
      CharacterPlate cp = new CharacterPlate(40, 8);
      cp.setMix(false);
      RectangleAlgorithm.drawRectangle(cp, new Rectangle(1, 0, this.maskWidth + 2, this.maskHeight + 2), RectangleStyle.NORMAL);
      RectangleAlgorithm.drawRectangle(cp, new Rectangle(11, 0, this.maskWidth + 2, this.maskHeight + 2), RectangleStyle.NORMAL);
      new CharacterPlate(this.mask).pasteInto(cp, 2, 1);
      new CharacterPlate(this.mask).pasteInto(cp, 12, 1);
      new CharacterPlate(this.result).pasteInto(cp, 12 - this.resultCenterX + this.maskCenterX, 1 - this.resultCenterY + this.maskCenterY);
      Insets in = cp.getEmptyInsets();
      cp.setSize(cp.getWidth(), cp.getHeight() - in.bottom);
      return cp.toString();
   }
}
