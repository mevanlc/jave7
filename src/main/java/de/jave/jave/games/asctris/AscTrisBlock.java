package de.jave.jave.games.asctris;

import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;

public class AscTrisBlock {
   public static final String GRADIENT = " :=8OSXZBEW#M";
   public int xPos;
   public int yPos;
   private int[][] pattern;
   private static final int[][][] PATTERNS = new int[][][]{
      {{'#', '#', ' '}, {' ', '#', '#'}},
      {{' ', '#', '#'}, {'#', '#', ' '}},
      {{' ', ' ', ' ', ' '}, {'#', '#', '#', '#'}, {' ', ' ', ' ', ' '}},
      {{' ', ' ', '#'}, {'#', '#', '#'}, {' ', ' ', ' '}},
      {{' ', ' ', ' '}, {'#', '#', '#'}, {' ', ' ', '#'}},
      {{'#', '#'}, {'#', '#'}},
      {{' ', '#', ' '}, {'#', '#', '#'}, {' ', ' ', ' '}}
   };
   private static final char[] COLORS = new char[]{'#', 'O', '8', 'M', ':', '=', 'E', 'W', 'S', 'B', 'X', 'Z'};

   public AscTrisBlock() {
      int type = Toolbox.random(0, PATTERNS.length - 1);
      this.pattern = PATTERNS[type];
      int color = Toolbox.random(0, COLORS.length - 1);
      char ch = COLORS[color];

      for (int y = 0; y < this.pattern.length; y++) {
         for (int x = 0; x < this.pattern[0].length; x++) {
            if (this.pattern[y][x] != ' ') {
               this.pattern[y][x] = ch;
            }
         }
      }

      int rotation = Toolbox.random(0, 3);

      for (int i = 0; i < rotation; i++) {
         this.rotateRight();
      }
   }

   public void rotateRight() {
      int w = this.pattern[0].length;
      int h = this.pattern.length;
      int[][] newPattern = new int[w][h];

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            newPattern[x][h - y - 1] = this.pattern[y][x];
         }
      }

      this.pattern = newPattern;
   }

   public void rotateLeft() {
      int w = this.pattern[0].length;
      int h = this.pattern.length;
      int[][] newPattern = new int[w][h];

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            newPattern[w - x - 1][y] = this.pattern[y][x];
         }
      }

      this.pattern = newPattern;
   }

   public boolean isInside() {
      for (int y = 0; y < this.pattern.length * 2; y++) {
         for (int x = 0; x < this.pattern[0].length * 3; x++) {
            if (this.yPos + y < 0 && this.pattern[y / 2][x / 3] != ' ') {
               return false;
            }
         }
      }

      return true;
   }

   public boolean fits(CharacterPlate cp) {
      int plateWidth = cp.getWidth();
      int plateHeight = cp.getHeight();

      for (int y = 0; y < this.pattern.length * 2; y++) {
         for (int x = 0; x < this.pattern[0].length * 3; x++) {
            if (this.pattern[y / 2][x / 3] != ' ') {
               if (this.yPos + y >= plateHeight) {
                  return false;
               }

               if (this.xPos + x >= plateWidth) {
                  return false;
               }

               if (this.xPos + x < 0) {
                  return false;
               }

               if (this.yPos + y >= 0 && cp.glyphAt(this.xPos + x, this.yPos + y) != ' ') {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public void pasteInto(CharacterPlate cp) {
      int h = this.pattern.length;
      int w = this.pattern[0].length;

      for (int y = 0; y < h * 2; y++) {
         for (int x = 0; x < w * 3; x++) {
            if (this.yPos + y >= 0 && this.pattern[y / 2][x / 3] != ' ') {
               cp.setForce(this.xPos + x, this.yPos + y, this.pattern[y / 2][x / 3]);
            }
         }
      }
   }

   public void removeFrom(CharacterPlate cp) {
      for (int y = 0; y < this.pattern.length * 2; y++) {
         for (int x = 0; x < this.pattern[0].length * 3; x++) {
            if (this.yPos + y >= 0 && this.pattern[y / 2][x / 3] != ' ') {
               cp.setForce(this.xPos + x, this.yPos + y, ' ');
            }
         }
      }
   }

   public void moveLeft() {
      this.xPos -= 3;
   }

   public void moveRight() {
      this.xPos += 3;
   }

   public void moveUp() {
      this.yPos -= 2;
   }

   public void moveDown() {
      this.yPos += 2;
   }
}
