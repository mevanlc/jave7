package de.jave.jave;

import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.tool.linealgorithmic.AlgorithmicLineStyle;
import java.awt.Point;

public class LineAlgorithm {
   private static final int DOWN = 0;
   private static final int UP = 1;
   private static final int HORIZONTAL = 0;
   private static final int VERTICAL = 1;
   protected static final char N_DOT = '+';
   protected static final char N_VERTICAL = '|';
   protected static final char N_HORIZONTAL = '-';
   protected static final String[] N_HORIZONTAL_UP = new String[]{"/", ".'", ".-'", "_.-'", "_.--'", "__.--'", "__..--'", "__..--''"};
   protected static final String[] N_HORIZONTAL_DOWN = new String[]{"\\", "`.", "`-.", "`-._", "`--._", "`--.__", "`--..__", "``--..__"};
   protected static final String[] N_VERTICAL_UP = new String[]{"/", "/|", "/||", "/|||", "/||||", "/|||||", "/||||||", "/|||||||"};
   protected static final String[] N_VERTICAL_DOWN = new String[]{"\\", "\\|", "\\||", "\\|||", "\\||||", "\\|||||", "\\||||||", "\\|||||||"};
   protected static final String N_HORIZONTAL_LEVELS_UP = "_.-'";
   protected static final String N_HORIZONTAL_LEVELS_DOWN = "`-._";
   protected static final String N_HORIZONTAL_LEVELS = "_.-'";
   protected static final char M_DOT = '+';
   protected static final char M_VERTICAL = '|';
   protected static final char M_HORIZONTAL = '-';
   protected static final String[] M_HORIZONTAL_UP = new String[]{"/", ",'", ",-\"", "_,-\"", "_,--\"", "__,--\"", "__,,--\"", "__,,,--\"\""};
   protected static final String[] M_HORIZONTAL_DOWN = new String[]{"\\", "`.", "`-.", "`-._", "\"--._", "\"--.__", "\"--..__", "\"\"--..__"};
   protected static final String[] M_VERTICAL_UP = new String[]{"/", "/|", "/||", "/|||", "/||||", "/|||||", "/||||||", "/|||||||"};
   protected static final String[] M_VERTICAL_DOWN = new String[]{"\\", "\\|", "\\||", "\\|||", "\\||||", "\\|||||", "\\||||||", "\\|||||||"};
   protected static final String M_HORIZONTAL_LEVELS_UP = "_,-\"";
   protected static final String M_HORIZONTAL_LEVELS_DOWN = "\"-._";
   protected static final String M_HORIZONTAL_LEVELS = "_.-\"";
   protected static final char G_DOT = '+';
   protected static final char G_VERTICAL = ':';
   protected static final char G_HORIZONTAL = '-';
   protected static final String[] G_HORIZONTAL_UP = new String[]{";", ",'", ",-'", ",-~'", ",--~'", ",,--~'"};
   protected static final String[] G_HORIZONTAL_DOWN = new String[]{":", "'.", "'-.", "'~-.", "'~--.", "'~--.."};
   protected static final String[] G_VERTICAL_UP = new String[]{";", ";:", ";::", ";:::", ";::::", ";:::::"};
   protected static final String[] G_VERTICAL_DOWN = new String[]{":", "::", ":::", "::::", ":::::", "::::::"};
   protected static final String G_HORIZONTAL_LEVELS_UP = ",-~'";
   protected static final String G_HORIZONTAL_LEVELS_DOWN = "'~-.";
   protected static final String G_HORIZONTAL_LEVELS = ".-~'";
   protected static final char S_DOT = '+';
   protected static final char S_VERTICAL = '|';
   protected static final char S_HORIZONTAL = '-';
   protected static final String[] S_HORIZONTAL_UP = new String[]{"/", ".'", ".-'", "_.-'", "_.--'", "__.--'", "__..--'", "__..--''"};
   protected static final String[] S_HORIZONTAL_DOWN = new String[]{"\\", "`.", "`-.", "`-._", "`--._", "`--.__", "`--..__", "\"\"--..__"};
   protected static final String[] S_VERTICAL_UP = new String[]{"/", "FJ", "F|J", "F||J", "FF|JJ", "FF||JJ"};
   protected static final String[] S_VERTICAL_DOWN = new String[]{"\\", "JL", "J|L", "J||L", "JJ|LL", "JJ||LL"};
   protected static final String S_HORIZONTAL_LEVELS_UP = "_.-\"";
   protected static final String S_HORIZONTAL_LEVELS_DOWN = "\"-._";
   protected static final String S_HORIZONTAL_LEVELS = "_.-\"";
   protected static final char C1_DOT = '+';
   protected static final char C1_VERTICAL = '|';
   protected static final char C1_HORIZONTAL = '-';
   protected static final String[] C1_HORIZONTAL_UP = new String[]{"/", ",\"", ",-\"", "_.-\"", "_.--\"", "__.--\"", "__..--\"", "__..--'\""};
   protected static final String[] C1_HORIZONTAL_DOWN = new String[]{"\\", "`.", "`-.", "`-._", "`--._", "`--.__", "`--..__", "``--..__"};
   protected static final String[] C1_VERTICAL_UP = new String[]{"',", "'|,", "'||,", "'|||,", "'||||,", "'|||||,", "'||||||,", "'|||||||,"};
   protected static final String[] C1_VERTICAL_DOWN = new String[]{"`.", "`|.", "`||.", "`|||.", "`||||.", "`|||||.", "`||||||.", "`|||||||."};
   protected static final String C1_HORIZONTAL_LEVELS_UP = "__..--'\"";
   protected static final String C1_HORIZONTAL_LEVELS_DOWN = "`-._";
   protected static final String C1_HORIZONTAL_LEVELS = "_.-'";
   protected static final char C2_DOT = '+';
   protected static final char C2_VERTICAL = '|';
   protected static final char C2_HORIZONTAL = '-';
   protected static final String[] C2_HORIZONTAL_UP = new String[]{"/", ",'", ".-'", "_,-\"", ",--'\"", ".--'\"\"", ".--''\"\"", "..--''\"\""};
   protected static final String[] C2_HORIZONTAL_DOWN = new String[]{"\\", "`.", "`-.", "`-._", "``--.", "\"\"`--.", "\"\"``--.", "\"\"``--.."};
   protected static final String[] C2_VERTICAL_UP = new String[]{"'.", "'|.", "'||.", "'|||.", "'||||.", "'|||||.", "'||||||.", "'|||||||."};
   protected static final String[] C2_VERTICAL_DOWN = new String[]{"`.", "`|.", "`||.", "`|||.", "`||||.", "`|||||.", "`||||||.", "`|||||||."};
   protected static final String C2_HORIZONTAL_LEVELS_UP = ".-'\"";
   protected static final String C2_HORIZONTAL_LEVELS_DOWN = "\"`-.";
   protected static final String C2_HORIZONTAL_LEVELS = ".-'";
   protected static final char B_DOT = '+';
   protected static final char B_VERTICAL = ':';
   protected static final char B_HORIZONTAL = '-';
   protected static final String[] B_HORIZONTAL_UP = new String[]{"/", ".'", ".-'", ".-*\"", "_.-*'", "_.-*\"'", "_.-=*\"'", "_.--=*\"'"};
   protected static final String[] B_HORIZONTAL_DOWN = new String[]{"\\", "`.", "`-.", "\"*-.", "`*-._", "`\"*-._", "`\"*=-._", "`\"*=--._"};
   protected static final String[] B_VERTICAL_UP = new String[]{"/", ";:", ";|:", ";||:", ";|||:", ";||||:", ";|||||:", ";||||||:"};
   protected static final String[] B_VERTICAL_DOWN = new String[]{"\\", ":;", ":|;", ":||;", ":|||;", ":||||;", ":|||||;", ":||||||;"};
   protected static final String B_HORIZONTAL_LEVELS_UP = "_.-=*\"'";
   protected static final String B_HORIZONTAL_LEVELS_DOWN = "`\"*=-._";
   protected static final String B_HORIZONTAL_LEVELS = "_.-'";

   protected static final String stretch(String s, int n) {
      int l = s.length();
      int delta = n - l;

      for (int i = delta; i > 0; i--) {
         int i1 = i * l / (delta + 1);
         s = s.substring(0, i1 + 1) + s.substring(i1);
      }

      return s;
   }

   public static void drawLine(PixelPlate plate, double x1, double y1, double x2, double y2, AlgorithmicLineStyle style) {
      drawLine(plate, x1, y1, x2, y2, style, false);
   }

   public static void drawLine(PixelPlate plate, Point2d p1, Point2d p2, AlgorithmicLineStyle style, boolean normalized) {
      drawLine(plate, p1.getX(), p1.getY(), p2.getX(), p2.getY(), style, normalized);
   }

   public static void drawLine(PixelPlate plate, double dx1, double dy1, double dx2, double dy2, AlgorithmicLineStyle style, boolean normalized) {
      char charDot = '+';
      char charVertical = '|';
      char charHorizontal = '-';
      String[] horizontalUp = N_HORIZONTAL_UP;
      String[] horizontalDown = N_HORIZONTAL_DOWN;
      String[] verticalUp = N_VERTICAL_UP;
      String[] verticalDown = N_VERTICAL_DOWN;
      String horizontalLevelsUp = "_.-'";
      String horizontalLevelsDown = "`-._";
      String horizontalLevels = "_.-'";
      if (style == AlgorithmicLineStyle.NORMAL) {
         charDot = '+';
         charVertical = '|';
         charHorizontal = '-';
         horizontalUp = M_HORIZONTAL_UP;
         horizontalDown = M_HORIZONTAL_DOWN;
         verticalUp = M_VERTICAL_UP;
         verticalDown = M_VERTICAL_DOWN;
         horizontalLevelsUp = "_,-\"";
         horizontalLevelsDown = "\"-._";
         horizontalLevels = "_.-\"";
      } else if (style == AlgorithmicLineStyle.GLORY) {
         charDot = '+';
         charVertical = ':';
         charHorizontal = '-';
         horizontalUp = G_HORIZONTAL_UP;
         horizontalDown = G_HORIZONTAL_DOWN;
         verticalUp = G_VERTICAL_UP;
         verticalDown = G_VERTICAL_DOWN;
         horizontalLevelsUp = ",-~'";
         horizontalLevelsDown = "'~-.";
         horizontalLevels = ".-~'";
      } else if (style == AlgorithmicLineStyle.SEGERMAN) {
         charDot = '+';
         charVertical = '|';
         charHorizontal = '-';
         horizontalUp = S_HORIZONTAL_UP;
         horizontalDown = S_HORIZONTAL_DOWN;
         verticalUp = S_VERTICAL_UP;
         verticalDown = S_VERTICAL_DOWN;
         horizontalLevelsUp = "_.-\"";
         horizontalLevelsDown = "\"-._";
         horizontalLevels = "_.-\"";
      } else if (style == AlgorithmicLineStyle.CEEJAY1) {
         charDot = '+';
         charVertical = '|';
         charHorizontal = '-';
         horizontalUp = C1_HORIZONTAL_UP;
         horizontalDown = C1_HORIZONTAL_DOWN;
         verticalUp = C1_VERTICAL_UP;
         verticalDown = C1_VERTICAL_DOWN;
         horizontalLevelsUp = "__..--'\"";
         horizontalLevelsDown = "`-._";
         horizontalLevels = "_.-'";
      } else if (style == AlgorithmicLineStyle.CEEJAY2) {
         charDot = '+';
         charVertical = '|';
         charHorizontal = '-';
         horizontalUp = C2_HORIZONTAL_UP;
         horizontalDown = C2_HORIZONTAL_DOWN;
         verticalUp = C2_VERTICAL_UP;
         verticalDown = C2_VERTICAL_DOWN;
         horizontalLevelsUp = ".-'\"";
         horizontalLevelsDown = "\"`-.";
         horizontalLevels = ".-'";
      } else if (style == AlgorithmicLineStyle.BUG) {
         charDot = '+';
         charVertical = ':';
         charHorizontal = '-';
         horizontalUp = B_HORIZONTAL_UP;
         horizontalDown = B_HORIZONTAL_DOWN;
         verticalUp = B_VERTICAL_UP;
         verticalDown = B_VERTICAL_DOWN;
         horizontalLevelsUp = "_.-=*\"'";
         horizontalLevelsDown = "`\"*=-._";
         horizontalLevels = "_.-'";
      }

      int x0;
      int y0;
      int x1;
      int y1;
      if (dx1 > dx2) {
         x0 = (int)Math.round(dx2);
         y0 = (int)Math.round(dy2);
         x1 = (int)Math.round(dx1);
         y1 = (int)Math.round(dy1);
         double t = dx1;
         dx1 = dx2;
         dx2 = t;
         t = dy1;
         dy1 = dy2;
         dy2 = t;
      } else {
         x1 = (int)Math.round(dx2);
         y1 = (int)Math.round(dy2);
         x0 = (int)Math.round(dx1);
         y0 = (int)Math.round(dy1);
      }

      int dx = x1 - x0;
      int dy = y1 - y0;
      if (dx == 0 && dy == 0) {
         drawLineDot(plate, x0, y0, charDot);
      } else if (dx == 0) {
         if (dy >= 0) {
            drawLineVertical(plate, x0, y0, y0 + dy, charVertical);
         } else {
            drawLineVertical(plate, x0, y0 + dy, y0, charVertical);
         }
      } else if (dy2 == dy1) {
         if (normalized) {
            drawLineHorizontal(plate, x0, x0 + dx, y0, charHorizontal);
         } else {
            double yOffset = 1.0 - (dy1 - (double)y1 + 0.5);
            int level = (int)((double)horizontalLevels.length() * yOffset);
            if (level > horizontalLevels.length() - 1) {
               level = horizontalLevels.length() - 1;
            }

            drawLineHorizontal(plate, x0, x0 + dx, y0, horizontalLevels.charAt(level));
         }
      } else {
         int adx = dx >= 0 ? dx : -dx;
         int ady = dy >= 0 ? dy : -dy;
         if (normalized && ady == 0) {
            drawLineHorizontal(plate, x0, x0 + dx, y0, charHorizontal);
         } else {
            int direction = 0;
            if (dy2 - dy1 < 0.0) {
               direction = 1;
            }

            int orientation = 0;
            if (adx < ady) {
               orientation = 1;
            }

            if (orientation == 0 && direction == 0) {
               if ((adx + 1) / (ady + 1) < 4 && ady != 0) {
                  int maxX = x1;
                  double s = (double)(dy + 1) / (double)(dx + 1);
                  double c = (0.5 - (double)x0) * s - (0.5 - (double)y0);
                  int[] ys = new int[dx + 1];

                  for (int x = 0; x <= dx; x++) {
                     ys[x] = (int)Math.round(s * (double)(x + x0) + c);
                  }

                  int d = 0;
                  int x = 0;
                  int oldY = 0;

                  for (x = 0; x <= dx; x++) {
                     d = 0;
                     oldY = ys[x];

                     while (x <= dx) {
                        if (ys[x] == oldY) {
                           d++;
                           x++;
                        } else {
                           drawLineHorizontalDown(plate, x0 + x - d, x0 + x - 1, oldY, maxX, horizontalDown);
                           d = 0;
                           oldY = ys[x];
                        }
                     }
                  }

                  drawLineHorizontalDown(plate, x0 + dx - d + 1, x0 + x - 2, oldY, maxX, horizontalDown);
               } else {
                  double s = (dy2 - dy1) / (dx2 - dx1);
                  double c = (dy1 * dx2 - dy2 * dx1) / (dx2 - dx1);

                  for (int x = 0; x <= dx; x++) {
                     double y = s * (double)(x + x0) + c;
                     int iy = (int)Math.round(y);
                     double yOffset = y - (double)iy + 0.5;
                     int level = (int)((double)horizontalLevelsDown.length() * yOffset);
                     if (level > horizontalLevelsDown.length() - 1) {
                        level = horizontalLevelsDown.length() - 1;
                     }

                     plate.set(x + x0, iy, horizontalLevelsDown.charAt(level));
                  }
               }
            } else if (orientation == 0 && direction == 1) {
               if ((adx + 1) / (ady + 1) < 4 && ady != 0) {
                  int maxX = x1;
                  double s = (double)(dy - 1) / (double)(dx + 1);
                  double c = (0.5 - (double)x0) * s + 0.5 + (double)y0;
                  int[] ys = new int[dx + 1];

                  for (int x = 0; x <= dx; x++) {
                     ys[x] = (int)Math.round(s * (double)(x + x0) + c);
                  }

                  int d = 0;
                  int x = 0;
                  int oldY = 0;

                  for (x = 0; x <= dx; x++) {
                     d = 0;
                     oldY = ys[x];

                     while (x <= dx) {
                        if (ys[x] == oldY) {
                           d++;
                           x++;
                        } else {
                           drawLineHorizontalUp(plate, x0 + x - d, x0 + x - 1, oldY, maxX, horizontalUp);
                           d = 0;
                           oldY = ys[x];
                        }
                     }
                  }

                  drawLineHorizontalUp(plate, x0 + dx - d + 1, x0 + x - 2, oldY, maxX, horizontalUp);
               } else {
                  double s = (dy2 - dy1) / (dx2 - dx1);
                  double c = (dy1 * dx2 - dy2 * dx1) / (dx2 - dx1);

                  for (int x = 0; x <= dx; x++) {
                     double y = s * (double)(x + x0) + c;
                     int iy = (int)Math.round(y);
                     double yOffset = y - (double)iy + 0.5;
                     int level = (int)((double)horizontalLevelsUp.length() * (1.0 - yOffset));
                     if (level > horizontalLevelsUp.length() - 1) {
                        level = horizontalLevelsUp.length() - 1;
                     }

                     plate.set(x + x0, iy, horizontalLevelsUp.charAt(level));
                  }
               }
            } else if (orientation == 1 && direction == 0) {
               double s = (double)(dx + 1) / (double)(dy + 1);
               double c = (0.5 - (double)y0) * s + (double)x0 - 0.5;
               int[] xs = new int[dy + 1];

               for (int y = 0; y <= dy; y++) {
                  xs[y] = (int)Math.round(s * (double)(y + y0) + c);
               }

               int d = 0;
               int y = 0;
               int oldX = 0;
               int minY = y0;

               for (y = 0; y <= dy; y++) {
                  d = 0;
                  oldX = xs[y];

                  while (y <= dy) {
                     if (xs[y] == oldX) {
                        d++;
                        y++;
                     } else {
                        drawLineVerticalDown(plate, oldX, y0 + y - d, y0 + y - 1, minY, verticalDown);
                        d = 0;
                        oldX = xs[y];
                     }
                  }
               }

               drawLineVerticalDown(plate, oldX, y0 + dy - d + 1, y0 + y - 2, minY, verticalDown);
            } else if (orientation == 1 && direction == 1) {
               double s = (double)(dx + 1) / (double)(dy - 1);
               double c = -(0.5 + (double)y0) * s + (double)x0 - 0.5;
               int[] xs = new int[ady + 1];
               int minY = y0 + dy;

               for (int y = 0; y <= ady; y++) {
                  xs[y] = (int)Math.round(s * (double)(y1 + y) + c);
               }

               int d = 0;
               int y = 0;
               int oldX = 0;

               for (y = 0; y <= ady; y++) {
                  d = 0;
                  oldX = xs[y];

                  while (y <= ady) {
                     if (xs[y] == oldX) {
                        d++;
                        y++;
                     } else {
                        drawLineVerticalUp(plate, oldX, y1 + y - d, y1 + y - 1, minY, verticalUp);
                        d = 0;
                        oldX = xs[y];
                     }
                  }
               }

               drawLineVerticalUp(plate, oldX, y1 - dy - d + 1, y1 + y - 2, minY, verticalUp);
            }
         }
      }
   }

   protected static void drawLineHorizontal(PixelPlate plate, int x1, int x2, int y, char ch) {
      for (int x = x1; x <= x2; x++) {
         plate.set(x, y, ch);
      }
   }

   protected static void drawLineHorizontalUp(PixelPlate plate, int x1, int x2, int y, int maxX, String[] horizontalUp) {
      int dx = x2 - x1;
      int l = horizontalUp.length;
      String pattern = "";
      if (dx >= l) {
         pattern = horizontalUp[l - 1];
         pattern = stretch(pattern, dx + 1);
      } else {
         pattern = horizontalUp[dx];
      }

      for (int i = 0; i < pattern.length() && x1 + i <= maxX; i++) {
         plate.set(x1 + i, y, pattern.charAt(i));
      }
   }

   protected static void drawLineHorizontalDown(PixelPlate plate, int x1, int x2, int y, int maxX, String[] horizontalDown) {
      int dx = x2 - x1;
      int l = horizontalDown.length;
      String pattern = "";
      if (dx >= l) {
         pattern = horizontalDown[l - 1];
         pattern = stretch(pattern, dx + 1);
      } else {
         pattern = horizontalDown[dx];
      }

      for (int i = 0; i < pattern.length() && x1 + i <= maxX; i++) {
         plate.set(x1 + i, y, pattern.charAt(i));
      }
   }

   protected static void drawLineVerticalUp(PixelPlate plate, int x, int y1, int y2, int minY, String[] verticalUp) {
      int dy = y2 - y1;
      int l = verticalUp.length;
      String pattern = "";
      if (dy >= l) {
         pattern = verticalUp[l - 1];
         pattern = stretch(pattern, dy + 1);
      } else {
         pattern = verticalUp[dy];
      }

      for (int i = 0; i < pattern.length() && y1 + dy - i >= minY; i++) {
         plate.set(x, y1 + dy - i, pattern.charAt(i));
      }
   }

   protected static void drawLineVerticalDown(PixelPlate plate, int x, int y1, int y2, int minY, String[] verticalDown) {
      int dy = y2 - y1;
      int l = verticalDown.length;
      String pattern = "";
      if (dy >= l) {
         pattern = verticalDown[l - 1];
         pattern = stretch(pattern, dy + 1);
      } else {
         pattern = verticalDown[dy];
      }

      for (int i = 0; i < pattern.length() && y1 + dy - i >= minY; i++) {
         plate.set(x, y1 + dy - i, pattern.charAt(i));
      }
   }

   protected static void drawLineVertical(PixelPlate plate, int x, int y1, int y2, char ch) {
      for (int y = y1; y <= y2; y++) {
         plate.set(x, y, ch);
      }
   }

   protected static void drawLineDot(PixelPlate plate, int x, int y, char ch) {
      plate.set(x, y, ch);
   }

   protected static void drawLineUp(PixelPlate plate, int x, int y, char charUp) {
      plate.set(x, y, charUp);
   }

   protected static void drawLineDown(PixelPlate plate, int x, int y, char charDown) {
      plate.set(x, y, charDown);
   }

   public static void drawLineBresenham(ICharacterDrawable plate, Point p1, Point p2, char ch) {
      drawLineBresenham(plate, p1.x, p1.y, p2.x, p2.y, ch);
   }

   public static void drawLineBresenham(ICharacterDrawable plate, int x1, int y1, int x2, int y2, char ch) {
      int x = x1;
      int y = y1;
      int d = 0;
      int hx = x2 - x1;
      int hy = y2 - y1;
      int xInc = 1;
      int yInc = 1;
      if (hx < 0) {
         xInc = -1;
         hx = -hx;
      }

      if (hy < 0) {
         yInc = -1;
         hy = -hy;
      }

      if (hy <= hx) {
         int c = 2 * hx;
         int m = 2 * hy;

         while (true) {
            plate.set(x, y, ch);
            if (x == x2) {
               break;
            }

            x += xInc;
            d += m;
            if (d > hx) {
               y += yInc;
               d -= c;
            }
         }
      } else {
         int c = 2 * hy;
         int m = 2 * hx;

         while (true) {
            plate.set(x, y, ch);
            if (y == y2) {
               break;
            }

            y += yInc;
            d += m;
            if (d > hy) {
               x += xInc;
               d -= c;
            }
         }
      }
   }
}
