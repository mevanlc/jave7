package de.jave.jave;

import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Labyrinth extends JaveGame {
   protected int width = 5;
   protected int height = 5;
   protected char[][] field;
   protected static final char WALL = ' ';
   protected static final char WAY = '.';
   protected static final char START = 'O';
   protected static final char EXIT = '*';
   protected static final int SOLUTION_POS_X = 31;
   protected static final int SOLUTION_POS_Y = 3;
   protected static final int PLATE_WIDTH = 45;
   protected static final int PLATE_HEIGHT = 21;
   protected int direction;
   protected Point position;
   protected static final int[] DX = new int[]{1, 0, -1, 0};
   protected static final int[] DY = new int[]{0, 1, 0, -1};
   protected static final int RIGHT = 0;
   protected static final int DOWN = 1;
   protected static final int LEFT = 2;
   protected static final int UP = 3;
   protected static final String[] DIRECTION_STR = new String[]{"RIGHT", "DOWN", "LEFT", "UP"};
   protected static final char[] DIRECTION_CHAR = new char[]{'>', 'V', '<', '^'};
   protected static final String BACKGROUND = "\\                         /\n  \\                     /  \n    \\                 /    \n      \\             /      \n        \\         /        \n          \\     /          \n            \\ /            \n            / \\            \n          /     \\          \n        /         \\        \n      /             \\      \n    /                 \\    \n  /                     \\  \n/                         \\\n";
   protected static final String EXIT_1 = "=========================\n. \\ EXIT ______ EXIT /  .\n.  `.   '------'   .`   .\n.    \\   .---..   /     .\n.  '' `-(_.-. `\\-` ''   .\n.  ''    .--' |'   ''   .\n.  ''   : ,-. |'   ''   .\n.  ''  J: `-' |'ve ''   .\n.  '' .-`.__:_,`-. ''   .\n.    /   ______   \\     .\n.  .'   '------'   `.   .\n=========================";
   protected static final String EXIT_2 = "===============\n...............\n..exit...exit..\n......\\-/......\n.....J|a|VE....\n..... /-\\......\n...............\n===============";
   protected static final String EXIT_3 = "============\n.exit..exit.\n....\\-/.....\n...J|a|VE...\n... /-\\.....\n============";
   protected static final String EXIT_4 = "========\n.e....e.\n...]a[..\n========";

   public Labyrinth(JavEApplication jave) {
      super(jave);
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(45, 21);
   }

   @Override
   public String getTitle() {
      return "Labyrinth 1.0";
   }

   @Override
   public ColorScheme getPreferredColorScheme() {
      return ColorScheme.BLACK_ON_GRAY;
   }

   public void startGame() {
      this.create();
      this.position = new Point(0, 1);
      this.direction = 0;
      this.plate.clear();
      this.plate.paste("Use cursor keys to navigate", 7, 17);
      RectangleAlgorithm.drawRectangle(this.plate, new Rectangle(0, 0, 29, 16), RectangleAlgorithm.getCharsForStyle(RectangleStyle.NORMAL));
      this.drawCurrentView();
      this.requestFocus();
   }

   protected void create() {
      this.field = new char[this.height * 2 + 1][this.width * 2 + 1];

      for (int y = 0; y < this.height * 2 + 1; y++) {
         for (int x = 0; x < this.width * 2 + 1; x++) {
            this.field[y][x] = ' ';
         }
      }

      this.field[1][1] = '.';

      boolean done;
      do {
         done = true;

         for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
               if (this.field[y * 2 + 1][x * 2 + 1] == '.') {
                  this.dig(x, y);
               } else {
                  done = false;
               }
            }
         }
      } while (!done);

      this.field[1][0] = 'O';
      this.field[this.height * 2 - 1][this.width * 2 - 1] = '*';
   }

   protected void dig(int x, int y) {
      int random = Toolbox.random(0, 3);
      int xx = x * 2 + 1;
      int yy = y * 2 + 1;

      for (int r = 0; r < 4; r++) {
         switch ((r + random) % 4) {
            case 0:
               if (y > 0 && this.field[yy - 2][xx] == ' ') {
                  this.field[yy - 1][xx] = '.';
                  this.field[yy - 2][xx] = '.';
                  this.dig(x, y - 1);
                  return;
               }
               break;
            case 1:
               if (x > 0 && this.field[yy][xx - 2] == ' ') {
                  this.field[yy][xx - 1] = '.';
                  this.field[yy][xx - 2] = '.';
                  this.dig(x - 1, y);
                  return;
               }
               break;
            case 2:
               if (y < this.height - 1 && this.field[yy + 2][xx] == ' ') {
                  this.field[yy + 1][xx] = '.';
                  this.field[yy + 2][xx] = '.';
                  this.dig(x, y + 1);
                  return;
               }
               break;
            case 3:
               if (x < this.width - 1 && this.field[yy][xx + 2] == ' ') {
                  this.field[yy][xx + 1] = '.';
                  this.field[yy][xx + 2] = '.';
                  this.dig(x + 1, y);
                  return;
               }
         }
      }
   }

   public void print() {
      for (int y = 0; y < this.height * 2 + 1; y++) {
         System.err.println(this.field[y]);
      }
   }

   protected void turnLeft() {
      this.direction = (this.direction + 3) % 4;
      this.drawCurrentView();
   }

   protected void turnRight() {
      this.direction = (this.direction + 1) % 4;
      this.drawCurrentView();
   }

   protected void moveForward() {
      int x1 = this.position.x + DX[this.direction];
      int y1 = this.position.y + DY[this.direction];
      if (x1 <= this.width * 2 && x1 >= 0 && y1 <= this.height * 2 && y1 >= 0 && (this.field[y1][x1] == '.' || this.field[y1][x1] == '*')) {
         this.plate.setForce(31 + this.position.x, 3 + this.position.y, this.field[this.position.y][this.position.x]);
         this.position.x = x1;
         this.position.y = y1;
         this.drawCurrentView();
         if (this.field[y1][x1] == '*') {
            RectangleAlgorithm.drawRectangle(this.plate, new Rectangle(7, 16, 30, 5), RectangleAlgorithm.getCharsForStyle(RectangleStyle.NORMAL));
            this.plate.paste("    * Congratulations *     ", 8, 17);
            this.plate.paste("  You have found the exit   ", 8, 18);
            this.plate.paste("Press S to start a new game.", 8, 19);
            this.repaint();
         }
      }
   }

   protected void drawCurrentView() {
      int x1 = this.position.x;
      int y1 = this.position.y;
      int x0 = this.position.x + DX[(this.direction + 3) % 4];
      int y0 = this.position.y + DY[(this.direction + 3) % 4];
      int x2 = this.position.x + DX[(this.direction + 1) % 4];
      int y2 = this.position.y + DY[(this.direction + 1) % 4];
      char[][] map = new char[5][3];

      for (int y = 0; x1 >= 0 && y1 >= 0 && x1 < this.width * 2 + 1 && y1 < this.height * 2 + 1 && this.field[y1][x1] != ' '; y++) {
         if (y < 5) {
            map[y][0] = ' ';
            map[y][1] = this.field[y1][x1];
            map[y][2] = ' ';
         }

         this.plate.setForce(31 + x1, 3 + y1, this.field[y1][x1]);
         if (x0 >= 0 && y0 >= 0 && x0 < this.width * 2 + 1 && y0 < this.height * 2 + 1) {
            if (y < 5) {
               map[y][0] = this.field[y0][x0];
            }

            this.plate.setForce(31 + x0, 3 + y0, this.field[y0][x0]);
         }

         if (x2 >= 0 && y2 >= 0 && x2 < this.width * 2 + 1 && y2 < this.height * 2 + 1) {
            if (y < 5) {
               map[y][2] = this.field[y2][x2];
            }

            this.plate.setForce(31 + x2, 3 + y2, this.field[y2][x2]);
         }

         x1 += DX[this.direction];
         y1 += DY[this.direction];
         x0 += DX[this.direction];
         y0 += DY[this.direction];
         x2 += DX[this.direction];
         y2 += DY[this.direction];
      }

      this.plate.paste("Exit", 31 + this.width * 2 - 1, 3 + this.height * 2 - 1);
      this.plate.setForce(31 + this.position.x, 3 + this.position.y, DIRECTION_CHAR[this.direction]);
      this.plate.fill(1, 1, 27, 14, ' ');
      CharacterPlate cp = new CharacterPlate(
         "\\                         /\n  \\                     /  \n    \\                 /    \n      \\             /      \n        \\         /        \n          \\     /          \n            \\ /            \n            / \\            \n          /     \\          \n        /         \\        \n      /             \\      \n    /                 \\    \n  /                     \\  \n/                         \\\n"
      );
      int depth = 1;

      while (depth < 5 && (map[depth][1] == '.' || map[depth][1] == '*')) {
         depth++;
      }

      boolean exit = map[depth - 1][1] == '*';
      switch (depth) {
         case 1:
            if (exit) {
               new CharacterPlate(
                     "=========================\n. \\ EXIT ______ EXIT /  .\n.  `.   '------'   .`   .\n.    \\   .---..   /     .\n.  '' `-(_.-. `\\-` ''   .\n.  ''    .--' |'   ''   .\n.  ''   : ,-. |'   ''   .\n.  ''  J: `-' |'ve ''   .\n.  '' .-`.__:_,`-. ''   .\n.    /   ______   \\     .\n.  .'   '------'   `.   .\n========================="
                  )
                  .pasteInto(cp, 2, 1);
            } else {
               cp.fill(2, 1, 23, 12, '=');
               cp.fill(2, 2, 23, 10, '.');
            }
            break;
         case 2:
            if (exit) {
               new CharacterPlate(
                     "===============\n...............\n..exit...exit..\n......\\-/......\n.....J|a|VE....\n..... /-\\......\n...............\n==============="
                  )
                  .pasteInto(cp, 6, 3);
            } else {
               cp.fill(6, 3, 15, 8, '=');
               cp.fill(6, 4, 15, 6, '.');
            }
            break;
         case 3:
            if (exit) {
               new CharacterPlate("============\n.exit..exit.\n....\\-/.....\n...J|a|VE...\n... /-\\.....\n============").pasteInto(cp, 8, 4);
            } else {
               cp.fill(8, 4, 11, 6, '=');
               cp.fill(8, 5, 11, 4, '.');
            }
            break;
         case 4:
            if (exit) {
               new CharacterPlate("========\n.e....e.\n...]a[..\n========").pasteInto(cp, 10, 5);
            } else {
               cp.fill(10, 5, 7, 4, '=');
               cp.fill(10, 6, 7, 2, '.');
            }
         case 5:
      }

      for (int i = depth - 1; i >= 0; i--) {
         if (map[i][2] == '.') {
            switch (i) {
               case 0:
                  if (i < depth - 1) {
                     cp.fill(24, 2, 1, 10, '|');
                  }

                  cp.setForce(26, 0, ' ');
                  cp.fill(25, 1, 2, 1, '=');
                  cp.fill(25, 2, 2, 10, '.');
                  cp.fill(25, 12, 2, 1, '=');
                  cp.setForce(26, 13, ' ');
                  break;
               case 1:
                  cp.fill(24, 2, 1, 10, '|');
                  if (i < depth - 1) {
                     cp.fill(20, 4, 1, 6, '|');
                  }

                  cp.fill(21, 3, 3, 1, '=');
                  cp.fill(21, 10, 3, 1, '=');
                  cp.fill(21, 4, 3, 6, '.');
                  cp.setForce(22, 2, ' ');
                  cp.setForce(22, 11, ' ');
                  break;
               case 2:
                  cp.fill(20, 4, 1, 6, '|');
                  if (i < depth - 1) {
                     cp.fill(18, 5, 1, 4, '|');
                  }

                  cp.setForce(19, 4, '=');
                  cp.setForce(19, 9, '=');
                  cp.fill(19, 5, 1, 4, '.');
                  break;
               case 3:
                  cp.fill(18, 5, 1, 4, '|');
                  cp.setForce(17, 5, '=');
                  cp.setForce(17, 8, '=');
                  cp.setForce(17, 6, '.');
                  cp.setForce(17, 7, '.');
            }
         }

         if (map[i][0] == '.') {
            switch (i) {
               case 0:
                  if (i < depth - 1) {
                     cp.fill(2, 2, 1, 10, '|');
                  }

                  cp.setForce(0, 0, ' ');
                  cp.fill(0, 1, 2, 1, '=');
                  cp.fill(0, 2, 2, 10, '.');
                  cp.fill(0, 12, 2, 1, '=');
                  cp.setForce(0, 13, ' ');
                  break;
               case 1:
                  cp.fill(2, 2, 1, 10, '|');
                  if (i < depth - 1) {
                     cp.fill(6, 4, 1, 6, '|');
                  }

                  cp.fill(3, 3, 3, 1, '=');
                  cp.fill(3, 10, 3, 1, '=');
                  cp.fill(3, 4, 3, 6, '.');
                  cp.setForce(4, 2, ' ');
                  cp.setForce(4, 11, ' ');
                  break;
               case 2:
                  cp.fill(6, 4, 1, 6, '|');
                  if (i < depth - 1) {
                     cp.fill(8, 5, 1, 4, '|');
                  }

                  cp.setForce(7, 4, '=');
                  cp.setForce(7, 9, '=');
                  cp.fill(7, 5, 1, 4, '.');
                  break;
               case 3:
                  cp.fill(8, 5, 1, 4, '|');
                  cp.setForce(9, 5, '=');
                  cp.setForce(9, 8, '=');
                  cp.setForce(9, 6, '.');
                  cp.setForce(9, 7, '.');
            }
         }
      }

      cp.pasteInto(this.plate, 1, 1);
      this.repaint();
   }

   @Override
   protected void keyTyped(KeyEvent evt, int code, char ch) {
      if (ch == 's') {
         this.startGame();
      } else if (ch == 'q') {
         this.quit();
      }
   }

   @Override
   protected void keyPressed(KeyEvent evt, int code, char ch) {
      if (code == 37) {
         this.turnLeft();
      } else if (code == 39) {
         this.turnRight();
      } else if (code == 38) {
         this.moveForward();
      }
   }

   public static void main(String[] args) {
      new Labyrinth(null);
   }
}
