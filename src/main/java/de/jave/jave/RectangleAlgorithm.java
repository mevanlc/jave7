package de.jave.jave;

import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.lib.CharacterPlate;
import java.awt.Rectangle;
import java.util.Arrays;

public class RectangleAlgorithm {
   /** One row of eight characters per {@link RectangleStyle}, in the order the styles are declared. */
   private static final char[][] RECTANGLE_CHARACTERS = new char[][]{
      {'+', '-', '+', '|', '|', '+', '-', '+'},
      {' ', '_', ' ', '|', '|', '|', '_', '|'},
      {'.', '-', '.', '|', '|', '`', '-', '\''},
      {'\\', '-', '/', '|', '|', '/', '-', '\\'},
      {'/', '-', '\\', '|', '|', '\\', '-', '/'},
      {'O', '-', 'O', '|', '|', 'O', '-', 'O'},
      {')', '-', '(', '|', '|', ')', '-', '('},
      {'┌', '─', '┐', '│', '│', '└', '─', '┘'},
      {'╭', '─', '╮', '│', '│', '╰', '─', '╯'},
      {'┏', '━', '┓', '┃', '┃', '┗', '━', '┛'},
      {'╔', '═', '╗', '║', '║', '╚', '═', '╝'},
      {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
   };
   private static char[] userDefinedChars = new char[]{'+', '-', '+', '|', '|', '+', '-', '+'};
   public static final int CHARACTERS = 8;

   public static void setUserDefinedChars(char[] chars) {
      userDefinedChars = chars;
   }

   public static char[] getCharsForStyle(RectangleStyle style) {
      if (style == null) {
         return userDefinedChars;
      } else {
         int index = Arrays.asList(RectangleStyle.values()).indexOf(style);
         return index == -1 ? userDefinedChars : RECTANGLE_CHARACTERS[index];
      }
   }

   public static RectangleStyle getRectangleStyle(int[][] content) {
      if (content == null) {
         return null;
      } else {
         int h = content.length;
         int w = content[0].length;
         if (w >= 3 && h >= 3) {
            int ch1 = content[0][0];
            int ch2 = content[0][1];
            int ch3 = content[0][w - 2];
            int ch4 = content[0][w - 1];
            int ch5 = content[1][0];
            int ch6 = content[1][w - 1];
            int ch7 = content[h - 2][0];
            int ch8 = content[h - 2][w - 1];
            int ch9 = content[h - 1][0];
            int ch10 = content[h - 1][1];
            int ch11 = content[h - 1][w - 2];
            int ch12 = content[h - 1][w - 1];
            if (ch2 == ch3 && ch5 == ch7 && ch6 == ch8 && ch10 == ch11) {
               if (ch2 != ' ' && ch5 != ' ' && ch6 != ' ' && ch10 != ' ') {
                  int[] patternChars = new int[]{ch1, ch2, ch4, ch5, ch6, ch9, ch10, ch12};
                  RectangleStyle[] styles = RectangleStyle.values();

                  for (int i = 0; i < styles.length; i++) {
                     char[] styleChars = getCharsForStyle(styles[i]);
                     boolean fits = true;

                     for (int j = 0; fits && j < styleChars.length; j++) {
                        fits = styleChars[j] == patternChars[j];
                     }

                     if (fits) {
                        return styles[i];
                     }
                  }

                  return null;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public static void drawRectangle(CharacterPlate plate, Rectangle region, RectangleStyle style) {
      drawRectangle(plate, region, getCharsForStyle(style));
   }

   public static void drawRectangle(CharacterPlate plate, Rectangle region, char[] characters) {
      int x1 = region.x;
      int y1 = region.y;
      int x2 = region.x + region.width - 1;
      int y2 = region.y + region.height - 1;

      for (int x = x1 + 1; x <= x2 - 1; x++) {
         plate.set(x, y1, characters[1]);
         plate.set(x, y2, characters[6]);
      }

      for (int y = y1 + 1; y <= y2 - 1; y++) {
         plate.set(x1, y, characters[3]);
         plate.set(x2, y, characters[4]);
      }

      plate.set(x1, y1, characters[0]);
      plate.set(x2, y1, characters[2]);
      plate.set(x1, y2, characters[5]);
      plate.set(x2, y2, characters[7]);
   }

   public static void drawRectangle(PixelPlate plate, Rectangle region, char[] characters) {
      int x1 = region.x;
      int y1 = region.y;
      int x2 = region.x + region.width - 1;
      int y2 = region.y + region.height - 1;

      for (int x = x1 + 1; x <= x2 - 1; x++) {
         plate.set(x, y1, characters[1]);
         plate.set(x, y2, characters[6]);
      }

      for (int y = y1 + 1; y <= y2 - 1; y++) {
         plate.set(x1, y, characters[3]);
         plate.set(x2, y, characters[4]);
      }

      plate.set(x1, y1, characters[0]);
      plate.set(x2, y1, characters[2]);
      plate.set(x1, y2, characters[5]);
      plate.set(x2, y2, characters[7]);
   }
}
