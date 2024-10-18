package de.jave.jave.games.asctris;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveGame;
import de.jave.jave.LineAlgorithm;
import de.jave.jave.RectangleAlgorithm;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.tool.linealgorithmic.AlgorithmicLineStyle;
import de.jave.lib.CharacterPlate;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import net.disy.commons.core.util.Ensure;

public class AscTris extends JaveGame implements Runnable {
   private static final String TITLE = "Asc-Tris 2.0";
   private AscTrisBlock currentBlock;
   private AscTrisBlock nextBlock;
   private static final int[] PAUSE_FOR_LEVEL = new int[]{800, 700, 600, 500, 410, 370, 335, 310, 290};
   private static final int MAX_LEVEL = 9;
   private static final int LINES_FOR_LEVEL = 6;
   private static final int[] SCORE_FOR_LINES = new int[]{40, 100, 300, 1200};
   private static final int START_POS_X = 12;
   private static final int START_POS_Y = 0;
   private static final int SCORE_X = 44;
   private static final int SCORE_Y = 3;
   private static final int LEVEL_X = 44;
   private static final int LEVEL_Y = 2;
   private static final int X0 = 3;
   private static final int Y0 = 0;
   private static final int X1 = 32;
   private static final int Y1 = 31;
   private static final int PLATE_WIDTH = 64;
   private static final int PLATE_HEIGHT = 33;
   private static final int PREVIEW_POS_X = 41;
   private static final int PREVIEW_POS_Y = 24;
   private boolean shallStop;
   private Thread thread;
   private boolean paused = false;
   private int score;
   private int level;
   private int dropStop;
   private final Object synchy = new Object();
   private final Filter filter;
   private CharacterPlate tmpBuffer;

   public AscTris(JavEApplication application, Filter filter) {
      super(application);
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(64, 33);
   }

   @Override
   protected String getTitle() {
      return "Asc-Tris 2.0";
   }

   @Override
   protected ColorScheme getPreferredColorScheme() {
      return ColorScheme.WHITE_ON_BLACK;
   }

   public void startGame() {
      this.shallStop = true;
      this.paused = false;

      while (this.thread != null) {
         this.thread.interrupt();

         try {
            Thread.sleep(20L);
         } catch (InterruptedException var3) {
         }
      }

      PixelPlate mp = new PixelPlate(new Rectangle(64, 33), this.filter);
      mp.setMode(PixelPlateMode.CHAR);
      LineAlgorithm.drawLine(mp, 2.0, 0.0, 2.0, 32.0, AlgorithmicLineStyle.VERONICA);
      LineAlgorithm.drawLine(mp, 33.0, 0.0, 33.0, 32.0, AlgorithmicLineStyle.VERONICA);
      LineAlgorithm.drawLine(mp, 2.0, 32.0, 33.0, 32.0, AlgorithmicLineStyle.VERONICA);
      LocatedCharacterPlate result = mp.convert();
      this.plate.clear();
      result.pasteInto(this.plate);
      this.plate.paste("~~~~~~~~~~~~~~", 36, 1);
      this.plate.paste("Score:", 37, 3);
      this.plate.paste("Level:", 37, 2);
      this.plate.paste("~~~~~~~~~~~~~~", 36, 4);
      this.plate.paste("Next:", 35, 23);
      this.plate.paste("  left: Cursor Left\n right: Cursor Right\n  drop: Space\nrotate: Cursor Up\n pause: P\n  quit: Q\nstart new game: S", 35, 6);
      this.plate
         .paste(
            ".-------.\n|       |\n|       |\n|       |\n|       |\n| _____ |\n||_   _||\n|  | |  |\n|  | |  |\n|  |_|  |\n| ____  |\n||  _ \\ |\n|| |_)/ |\n|| ,-.\\ |\n||_| |_\\|\n'   _   |\n   | |  |\n   | |  |\n   | |  |\n.  |_|  |\n|       |\n| .----.|\n|/   __/|\n|\\___  \\|\n|/     /|\n|`----' |\n|       |\n|   _   |\n| .\\_/. |\n| ||a|| |\n| `/_\\' |\n|       |\n`-------'",
            55,
            0
         );
      this.plate.paste(",---------------\n| .-. .-.  .-.\n| .-| `-. (   <>\n| `-^ `-'  `-'\n`---------------", 39, 15);
      this.shallStop = false;
      this.thread = new Thread(this, "AscTris");
      this.thread.start();
      this.requestFocus();
   }

   @Override
   protected void keyTyped(KeyEvent evt, int code, char ch) {
      if (this.paused) {
         this.plate.delete(4, 11, 30, 14);
         this.tmpBuffer.pasteInto(this.plate, 4, 11);
         this.repaint();
         this.paused = false;
      } else if (ch == 'p' && this.thread != null && !this.shallStop) {
         this.paused = true;
         this.tmpBuffer = this.plate.getCopy(4, 11, 27, 4);
         RectangleAlgorithm.drawRectangle(this.plate, new Rectangle(4, 11, 27, 4), RectangleAlgorithm.getCharsForStyle(RectangleStyle.NORMAL));
         this.plate.paste("       * Paused *        ", 5, 12);
         this.plate.paste(" Hit any key to continue ", 5, 13);
         this.repaint();
      } else if (ch == 's') {
         this.startGame();
      } else if (ch == 'q') {
         this.shallStop = true;
         this.thread = null;
         this.quit();
      }
   }

   @Override
   protected void keyPressed(KeyEvent evt, int code, char ch) {
      if (this.thread != null && !this.shallStop && !this.paused) {
         if (ch == ' ') {
            this.drop();
         } else if (code == 37) {
            this.moveLeft();
         } else if (code == 39) {
            this.moveRight();
         } else if (code == 40) {
            this.down();
         } else if (code == 38) {
            this.rotateLeft();
         }
      }
   }

   @Override
   public void run() {
      this.currentBlock = new AscTrisBlock();
      this.currentBlock.xPos = 12;
      this.currentBlock.yPos = 0;
      this.currentBlock.pasteInto(this.plate);
      this.nextBlock = new AscTrisBlock();
      this.nextBlock.xPos = 41;
      this.nextBlock.yPos = 24;
      this.nextBlock.pasteInto(this.plate);
      this.dropStop = 1;
      this.setLevel(1);
      this.resetScore();
      int lineCount = 0;
      long time1 = System.currentTimeMillis();

      while (!this.shallStop) {
         long wait = (long)PAUSE_FOR_LEVEL[this.level - 1] - (System.currentTimeMillis() - time1);
         if (wait > 0L) {
            try {
               Thread.sleep(wait);
            } catch (InterruptedException var14) {
            }
         }

         while ((!this.enabled || this.paused) && !this.shallStop) {
            try {
               Thread.sleep(50L);
            } catch (InterruptedException var13) {
            }
         }

         time1 = System.currentTimeMillis();
         if (this.shallStop) {
            break;
         }

         synchronized (this.synchy) {
            this.currentBlock.removeFrom(this.plate);
            this.currentBlock.moveDown();
            if (this.currentBlock.fits(this.plate)) {
               this.currentBlock.pasteInto(this.plate);
               this.dropStop--;
            } else {
               this.dropStop = 1;
               this.currentBlock.moveUp();
               this.currentBlock.pasteInto(this.plate);
               Thread.yield();
               int countFull = 0;

               for (int y = 0; y <= 31; y += 2) {
                  boolean full = true;

                  for (int x = 3; x <= 32 && full; x++) {
                     if (this.plate.get(x, y) == ' ') {
                        full = false;
                     }
                  }

                  if (full) {
                     countFull++;
                     lineCount++;
                     this.fadeLine(y);

                     for (int yy = y - 1; yy >= 0; yy--) {
                        for (int xx = 3; xx <= 32; xx++) {
                           this.plate.setForce(xx, yy + 2, this.plate.get(xx, yy));
                        }
                     }
                  }
               }

               if (countFull > 0) {
                  if (countFull - 1 >= SCORE_FOR_LINES.length) {
                     countFull = SCORE_FOR_LINES.length;
                  }

                  this.addScore(this.level * SCORE_FOR_LINES[countFull - 1]);
               }

               int newLevel = lineCount / 6;
               if (newLevel <= 9 && newLevel > this.level) {
                  this.setLevel(newLevel);
               }

               this.nextBlock.removeFrom(this.plate);
               this.currentBlock = this.nextBlock;
               this.currentBlock.xPos = 12;
               this.currentBlock.yPos = 0;

               while (this.currentBlock.isInside()) {
                  this.currentBlock.moveUp();
               }

               this.currentBlock.moveDown();
               if (!this.currentBlock.fits(this.plate)) {
                  RectangleAlgorithm.drawRectangle(this.plate, new Rectangle(10, 12, 17, 3), RectangleAlgorithm.getCharsForStyle(RectangleStyle.NORMAL));
                  this.plate.paste(" * Game Over * ", 11, 13);
                  this.shallStop = true;
                  this.thread = null;
                  this.repaint();
               } else {
                  this.currentBlock.pasteInto(this.plate);
                  this.nextBlock = new AscTrisBlock();
                  this.nextBlock.xPos = 41;
                  this.nextBlock.yPos = 24;
                  this.nextBlock.pasteInto(this.plate);
               }
            }

            this.repaint();
         }
      }

      this.document.setModified(false);
      this.thread = null;
   }

   protected void fadeLine(int y) {
      for (int i = 0; i < 6; i++) {
         for (int x = 3; x <= 32; x++) {
            for (int line = 0; line <= 1; line++) {
               int shade = " :=8OSXZBEW#M".indexOf(this.plate.get(x, y + line));
               if (shade < 0) {
                  shade = 0;
               }

               shade /= 2;
               this.plate.setForce(x, y + line, " :=8OSXZBEW#M".charAt(shade));
            }
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var6) {
         }

         this.repaint();
      }
   }

   protected void rotateLeft() {
      synchronized (this.synchy) {
         this.currentBlock.removeFrom(this.plate);
         this.currentBlock.rotateLeft();
         if (!this.currentBlock.fits(this.plate)) {
            this.currentBlock.moveRight();
            if (!this.currentBlock.fits(this.plate)) {
               this.currentBlock.moveLeft();
            }
         }

         if (!this.currentBlock.fits(this.plate)) {
            this.currentBlock.moveLeft();
            if (!this.currentBlock.fits(this.plate)) {
               this.currentBlock.moveRight();
            }
         }

         if (this.currentBlock.fits(this.plate)) {
            this.currentBlock.rotateRight();
            this.currentBlock.removeFrom(this.plate);
            this.currentBlock.rotateLeft();
            this.currentBlock.pasteInto(this.plate);
            this.repaint();
         } else {
            this.currentBlock.rotateRight();
            this.currentBlock.pasteInto(this.plate);
         }
      }
   }

   protected void rotateRight() {
      synchronized (this.synchy) {
         this.currentBlock.removeFrom(this.plate);
         this.currentBlock.rotateRight();
         if (!this.currentBlock.fits(this.plate)) {
            this.currentBlock.moveRight();
            if (!this.currentBlock.fits(this.plate)) {
               this.currentBlock.moveLeft();
            }
         }

         if (!this.currentBlock.fits(this.plate)) {
            this.currentBlock.moveLeft();
            if (!this.currentBlock.fits(this.plate)) {
               this.currentBlock.moveRight();
            }
         }

         if (this.currentBlock.fits(this.plate)) {
            this.currentBlock.rotateLeft();
            this.currentBlock.removeFrom(this.plate);
            this.currentBlock.rotateRight();
            this.currentBlock.pasteInto(this.plate);
            this.repaint();
         } else {
            this.currentBlock.rotateLeft();
            this.currentBlock.pasteInto(this.plate);
         }
      }
   }

   protected void moveLeft() {
      synchronized (this.synchy) {
         this.currentBlock.removeFrom(this.plate);
         this.currentBlock.moveLeft();
         if (this.currentBlock.fits(this.plate)) {
            this.currentBlock.moveRight();
            this.currentBlock.removeFrom(this.plate);
            this.currentBlock.moveLeft();
            this.currentBlock.pasteInto(this.plate);
            this.repaint();
         } else {
            this.currentBlock.moveRight();
            this.currentBlock.pasteInto(this.plate);
         }
      }
   }

   protected void moveRight() {
      synchronized (this.synchy) {
         this.currentBlock.removeFrom(this.plate);
         this.currentBlock.moveRight();
         if (this.currentBlock.fits(this.plate)) {
            this.currentBlock.moveLeft();
            this.currentBlock.removeFrom(this.plate);
            this.currentBlock.moveRight();
            this.currentBlock.pasteInto(this.plate);
            this.repaint();
         } else {
            this.currentBlock.moveLeft();
            this.currentBlock.pasteInto(this.plate);
         }
      }
   }

   protected void down() {
      this.thread.interrupt();
      this.addScore(1);
   }

   protected void drop() {
      if (this.dropStop <= 0) {
         synchronized (this.synchy) {
            this.currentBlock.removeFrom(this.plate);
            int count = 0;

            do {
               this.currentBlock.moveDown();
               count++;
            } while (this.currentBlock.fits(this.plate));

            this.currentBlock.moveUp();
            this.addScore(--count);
            this.repaint();
         }

         this.thread.interrupt();
      }
   }

   protected void addScore(int value) {
      this.setScore(this.score + value);
   }

   protected void resetScore() {
      this.setScore(0);
   }

   protected void setScore(int value) {
      this.score = value;
      this.plate.paste(this.score + "   ", 44, 3);
      this.repaint();
   }

   protected void setLevel(int value) {
      this.level = value;
      this.plate.paste(this.level + "   ", 44, 2);
      this.repaint();
   }
}
