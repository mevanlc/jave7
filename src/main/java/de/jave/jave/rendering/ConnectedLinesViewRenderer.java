package de.jave.jave.rendering;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.tool.text.RowRange;
import de.jave.lib.CharacterPlate;
import java.awt.Graphics;
import java.awt.Point;

public class ConnectedLinesViewRenderer {
   public static final void paintConnectedLinesView(Graphics g, CharacterPlate content, CharacterMetrics metrics) {
      paintConnectedLinesView(g, content, new RowRange(0, content.getHeight() - 1), new Point(0, 0), metrics);
   }

   public static final void paintConnectedLinesView(Graphics g, CharacterPlate content, RowRange rowRange, Point plateOrigin, CharacterMetrics metrics) {
      int width = content.getWidth();
      int height = content.getHeight();

      for (int i = rowRange.getRowStartIndex(); i <= rowRange.getRowEndIndex(); i++) {
         for (int j = 0; j < width; j++) {
            char ch = content.get(j, i);
            char chN = ' ';
            if (i > 0) {
               chN = content.get(j, i - 1);
            }

            char chS = ' ';
            if (i < height - 1) {
               chS = content.get(j, i + 1);
            }

            char chW = ' ';
            if (j > 0) {
               chW = content.get(j - 1, i);
            }

            char chE = ' ';
            if (j < width - 1) {
               chE = content.get(j + 1, i);
            }

            if (!paintPixelChar(
                  g,
                  plateOrigin.x + j * metrics.getWidth(),
                  plateOrigin.y + i * metrics.getHeight(),
                  metrics.getWidth(),
                  metrics.getHeight(),
                  ch,
                  chN,
                  chS,
                  chW,
                  chE
               )
               && ch > ' ') {
               g.drawString(String.valueOf(ch), plateOrigin.x + j * metrics.getWidth(), plateOrigin.y + i * metrics.getHeight() + metrics.getAscent());
            }
         }
      }
   }

   private static final boolean paintPixelChar(Graphics g, int x0, int y0, int charWidth, int charHeight, char ch, char chN, char chS, char chW, char chE) {
      boolean painted = false;
      if (ch == '.' && chS == '|' && chE == '-') {
         paintRoundCornerNW(g, x0, y0, charWidth, charHeight);
         return true;
      } else if (ch == '.' && chS == '|' && chW == '-') {
         paintRoundCornerNE(g, x0, y0, charWidth, charHeight);
         return true;
      } else if (ch == '\'' && chN == '|' && chW == '-') {
         paintRoundCornerSE(g, x0, y0, charWidth, charHeight);
         return true;
      } else if (ch == '`' && chN == '|' && chE == '-') {
         paintRoundCornerSW(g, x0, y0, charWidth, charHeight);
         return true;
      } else if (ch == ' ') {
         if (chS == '|' && chE == '_') {
            paintLine15(g, x0, y0, charWidth, charHeight);
         }

         if (chS == '|' && chW == '_') {
            paintLine16(g, x0, y0, charWidth, charHeight);
         }

         return true;
      } else {
         if (ch == '-') {
            if (chE == '+'
               || chE == '-'
               || chE == '>'
               || chW == '+'
               || chW == '+'
               || chW == '<'
               || chE == '.'
               || chE == '\''
               || chE == '/'
               || chE == '\\'
               || chE == '('
               || chE == 'O') {
               paintLine13(g, x0, y0, charWidth, charHeight);
               return true;
            }
         } else {
            if (ch == '_') {
               paintLine14(g, x0, y0, charWidth, charHeight);
               return true;
            }

            if (ch == '|') {
               paintLine12(g, x0, y0, charWidth, charHeight);
               if (chE == '_') {
                  paintLine15(g, x0, y0, charWidth, charHeight);
               }

               if (chW == '_') {
                  paintLine16(g, x0, y0, charWidth, charHeight);
               }

               return true;
            }

            if (ch == '/') {
               if (chE == '-') {
                  if (chS == '|') {
                     paintLine4(g, x0, y0, charWidth, charHeight);
                     painted = true;
                  }

                  if (chN == '|') {
                     paintLine10(g, x0, y0, charWidth, charHeight);
                     painted = true;
                  }
               }

               if (chW == '-') {
                  if (chN == '|') {
                     paintLine6(g, x0, y0, charWidth, charHeight);
                     painted = true;
                  }

                  if (chS == '|') {
                     paintLine9(g, x0, y0, charWidth, charHeight);
                     painted = true;
                  }
               }
            } else if (ch == '\\') {
               if (chE == '-' && chN == '|') {
                  paintLine7(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }

               if (chW == '-' && chS == '|') {
                  paintLine5(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }

               if (chE == '-' && chS == '|') {
                  paintLine8(g, x0, y0, charWidth, charHeight);
                  painted = true;
               } else if (chW == '-' && chN == '|') {
                  paintLine11(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }
            } else if (ch == '+') {
               if (chW == '-' && chN == '|') {
                  paintLine0(g, x0, y0, charWidth, charHeight);
                  paintLine1(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }

               if (chW == '-' && chS == '|') {
                  paintLine0(g, x0, y0, charWidth, charHeight);
                  paintLine3(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }

               if (chE == '-' && chN == '|') {
                  paintLine2(g, x0, y0, charWidth, charHeight);
                  paintLine1(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }

               if (chE == '-' && chS == '|') {
                  paintLine2(g, x0, y0, charWidth, charHeight);
                  paintLine3(g, x0, y0, charWidth, charHeight);
                  painted = true;
               }
            } else if (ch == '<') {
               if (chE == '-') {
                  paintLine2(g, x0, y0, charWidth, charHeight);
               }
            } else if (ch == '>' && chW == '-') {
               paintLine0(g, x0, y0, charWidth, charHeight);
            }
         }

         return painted;
      }
   }

   private static void paintLine0(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y + h / 2, x + w / 2, y + h / 2);
   }

   private static void paintLine1(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w / 2, y, x + w / 2, y + h / 2);
   }

   private static void paintLine2(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w, y + h / 2, x + w / 2, y + h / 2);
   }

   private static void paintLine3(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w / 2, y + h / 2, x + w / 2, y + h);
   }

   private static void paintLine4(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w / 2, y + h, x + w, y + h / 2);
   }

   private static void paintLine5(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w / 2, y + h, x, y + h / 2);
   }

   private static void paintLine6(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y + h / 2, x + w / 2, y);
   }

   private static void paintLine7(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w, y + h / 2, x + w / 2, y);
   }

   private static void paintLine8(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y, x + w, y + h / 2);
      g.drawLine(x, y, x + w / 2, y + h);
   }

   private static void paintLine9(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w, y, x, y + h / 2);
      g.drawLine(x + w, y, x + w / 2, y + h);
   }

   private static void paintLine10(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y + h, x + w, y + h / 2);
      g.drawLine(x, y + h, x + w / 2, y);
   }

   private static void paintLine11(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w, y + h, x, y + h / 2);
      g.drawLine(x + w, y + h, x + w / 2, y);
   }

   private static void paintLine12(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w / 2, y, x + w / 2, y + h);
   }

   private static void paintLine13(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y + h / 2, x + w, y + h / 2);
   }

   private static void paintLine14(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y + h, x + w, y + h);
   }

   private static void paintLine15(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x + w, y + h, x + w / 2, y + h);
   }

   private static void paintLine16(Graphics g, int x, int y, int w, int h) {
      g.drawLine(x, y + h, x + w / 2, y + h);
   }

   private static void paintRoundCornerNW(Graphics g, int x, int y, int w, int h) {
      g.drawArc(x + w / 2, y + h / 2, w, h, 90, 90);
   }

   private static void paintRoundCornerNE(Graphics g, int x, int y, int w, int h) {
      g.drawArc(x - w / 2, y + h / 2, w, h, 0, 90);
   }

   private static void paintRoundCornerSW(Graphics g, int x, int y, int w, int h) {
      g.drawArc(x + w / 2, y - h / 2, w, h, 180, 90);
   }

   private static void paintRoundCornerSE(Graphics g, int x, int y, int w, int h) {
      g.drawArc(x - w / 2, y - h / 2, w, h, 270, 90);
   }
}
