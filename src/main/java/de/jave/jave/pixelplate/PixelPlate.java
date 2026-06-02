package de.jave.jave.pixelplate;

import de.jave.jave.ICharacterDrawable;
import de.jave.jave.Point2d;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterMode;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Rectangle;
import net.dizzy.commons.core.util.Ensure;

public class PixelPlate implements ICharacterDrawable {
   private static final char[] TWO_BY_TWO_CHARS = new char[]{' ', '\'', '.', '(', '`', '"', '/', 'P', ',', '\\', '_', 'L', ')', '7', 'J', '8'};
   private static final char[] THREE_BY_TWO_CHARS = new char[]{
      ' ',
      '`',
      '-',
      '!',
      '.',
      '|',
      'i',
      '[',
      '\'',
      '~',
      '/',
      'f',
      '/',
      '7',
      '/',
      'P',
      '-',
      '\\',
      '=',
      '+',
      'v',
      ')',
      'z',
      'D',
      '!',
      'V',
      'Y',
      '*',
      '/',
      'Z',
      'Z',
      'A',
      ',',
      '\\',
      'c',
      't',
      '_',
      'L',
      's',
      'b',
      '!',
      'T',
      '(',
      '5',
      '2',
      'X',
      'K',
      'K',
      'i',
      '\\',
      'e',
      'N',
      'g',
      'G',
      'm',
      'W',
      ']',
      'Y',
      '4',
      'M',
      'd',
      '8',
      'W',
      '@'
   };
   public static final char[] FELTPEN_CHARS = new char[]{'!', ':', '$', 'O', '8', 'X', 'M'};
   public static final int DEFAULT_FELTPEN_CHAR_INDEX = 4;
   public static PixelPlateMode[] FELTPEN_GRADIENT_MODES = new PixelPlateMode[]{
      PixelPlateFeltPenMode.FELTPEN_EXCLAM,
      PixelPlateFeltPenMode.FELTPEN_COLON,
      PixelPlateFeltPenMode.FELTPEN_DOLLAR,
      PixelPlateFeltPenMode.FELTPEN_O,
      PixelPlateFeltPenMode.FELTPEN_X,
      PixelPlateFeltPenMode.FELTPEN_8,
      PixelPlateFeltPenMode.FELTPEN_M
   };
   private PixelPlateMode mode;
   private int pencilSize;
   private char character;
   private char feltPenChar;
   private char[][] pixels;
   private Rectangle bounds;
   private final Rectangle maximumBounds;
   private final Filter filter;
   private static final char[] DOT_TABLE = new char[]{'\u0000', '\'', '.', ':'};

   public PixelPlate(Rectangle finalBounds, Filter filter) {
      this(finalBounds, finalBounds, filter);
   }

   public PixelPlate(Rectangle bounds, Rectangle maximumBounds, Filter filter) {
      Ensure.ensureArgumentNotNull(bounds);
      Ensure.ensureArgumentNotNull(maximumBounds);
      Ensure.ensureArgumentNotNull(filter);
      this.bounds = bounds;
      this.maximumBounds = maximumBounds;
      this.filter = filter;
      this.pixels = new char[bounds.height][bounds.width];
      this.character = 'X';
      this.setMode(PixelPlateMode.PIXEL);
   }

   public void setMode(PixelPlateMode mode) {
      Ensure.ensureArgumentNotNull(mode);
      this.mode = mode;
      if (mode == PixelPlateMode.PIXEL) {
         this.feltPenChar = ' ';
         this.pencilSize = 1;
      } else if (mode == PixelPlateMode.CHAR) {
         this.feltPenChar = ' ';
         this.pencilSize = 1;
      } else if (mode instanceof PixelPlateFeltPenMode) {
         PixelPlateFeltPenMode feltPenMode = (PixelPlateFeltPenMode)mode;
         this.feltPenChar = feltPenMode.getCharacter();
         this.pencilSize = feltPenMode.getSize() + 1;
      } else if (mode == PixelPlateMode.TWO_BY_TWO) {
         this.feltPenChar = ' ';
         this.pencilSize = 1;
      } else if (mode == PixelPlateMode.THREE_BY_TWO) {
         this.feltPenChar = ' ';
         this.pencilSize = 1;
      } else if (mode == PixelPlateMode.THICK_THIN) {
         this.feltPenChar = '8';
         this.pencilSize = 1;
      } else {
         if (mode != PixelPlateMode.DOT) {
            throw new IllegalArgumentException("Unsupported mode " + mode);
         }

         this.feltPenChar = ' ';
         this.pencilSize = 1;
      }
   }

   public int getWidth() {
      return this.bounds.width;
   }

   public int getVirtualWidth() {
      return this.bounds.width * this.getRasterX();
   }

   public int getHeight() {
      return this.bounds.height;
   }

   public int getVirtualHeight() {
      return this.bounds.height * this.getRasterY();
   }

   public int getOriginX() {
      return this.bounds.x;
   }

   public int getOriginY() {
      return this.bounds.y;
   }

   public void removeNoise(int max) {
      int w = this.getVirtualWidth();
      int h = this.getVirtualHeight();
      boolean[][] kill = new boolean[h][w];

      for (int x = 0; x < w; x++) {
         for (int y = 0; y < h; y++) {
            kill[y][x] = false;
            int neighbourCount = 0;
            if (y - 1 >= 0 && this.isSet(x, y - 1)) {
               neighbourCount++;
            }

            if (neighbourCount <= max) {
               if (y - 1 >= 0 && x + 1 < w && this.isSet(x + 1, y - 1)) {
                  neighbourCount++;
               }

               if (neighbourCount <= max) {
                  if (x + 1 < w && this.isSet(x + 1, y)) {
                     neighbourCount++;
                  }

                  if (neighbourCount <= max) {
                     if (y + 1 < h && x + 1 < w && this.isSet(x + 1, y + 1)) {
                        neighbourCount++;
                     }

                     if (neighbourCount <= max) {
                        if (y + 1 < h && this.isSet(x, y + 1)) {
                           neighbourCount++;
                        }

                        if (neighbourCount <= max) {
                           if (y + 1 < h && x - 1 >= 0 && this.isSet(x - 1, y + 1)) {
                              neighbourCount++;
                           }

                           if (neighbourCount <= max) {
                              if (x - 1 >= 0 && this.isSet(x - 1, y)) {
                                 neighbourCount++;
                              }

                              if (neighbourCount <= max) {
                                 if (y - 1 >= 0 && x - 1 >= 0 && this.isSet(x - 1, y - 1)) {
                                    neighbourCount++;
                                 }

                                 if (neighbourCount <= max) {
                                    kill[y][x] = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      for (int x = 0; x < w; x++) {
         for (int y = 0; y < h; y++) {
            if (kill[y][x]) {
               this.setInternal(x, y, false);
            }
         }
      }
   }

   public void setCharacter(char ch) {
      this.character = ch;
   }

   public char getCharacter() {
      return this.character;
   }

   public void clear() {
      if (!this.bounds.isEmpty()) {
         for (int x = 0; x < this.bounds.width; x++) {
            this.pixels[0][x] = 0;
         }

         for (int y = 1; y < this.bounds.height; y++) {
            System.arraycopy(this.pixels[0], 0, this.pixels[y], 0, this.bounds.width);
         }
      }
   }

   public int getPhysicalX(double x) {
      return (int)(x * (double)this.getRasterX());
   }

   public int getPhysicalY(double y) {
      return (int)(y * (double)this.getRasterY());
   }

   public void drawEllipse(double oX, double oY, double radiusX, double radiusY) {
      int xx = this.getPhysicalX(oX);
      int yy = this.getPhysicalY(oY);
      int rX = this.getPhysicalX(radiusX);
      int rY = this.getPhysicalY(radiusY);
      this.drawEllipse(xx, yy, rX, rY);
   }

   public void fillEllipse(double oX, double oY, double radiusX, double radiusY) {
      int xx = this.getPhysicalX(oX);
      int yy = this.getPhysicalY(oY);
      int rX = this.getPhysicalX(radiusX);
      int rY = this.getPhysicalY(radiusY);
      this.fillEllipse(xx, yy, rX, rY);
   }

   private void drawEllipse(int oX, int oY, int radiusX, int radiusY) {
      if (radiusX == 0) {
         this.drawLineBresenham(oX, oY - radiusY, oX, oY + radiusY);
      } else if (radiusY == 0) {
         this.drawLineBresenham(oX - radiusX, oY, oX + radiusX, oY);
      } else {
         if (radiusY <= radiusX) {
            int x = 0;
            int y = radiusY;
            int xE = 0;
            int yE = radiusX * radiusX;
            int e = -yE / 2;
            int c = yE / radiusY;

            do {
               if (e <= 0) {
                  do {
                     this.set(oX + x, oY + y);
                     this.set(oX - x, oY + y);
                     this.set(oX + x, oY - y);
                     this.set(oX - x, oY - y);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX + x, oY + y);
                  this.set(oX - x, oY + y);
                  this.set(oX + x, oY - y);
                  this.set(oX - x, oY - y);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX + x, oY);
            this.set(oX - x, oY);
         } else {
            int x = 0;
            int y = radiusX;
            int xE = 0;
            int yE = radiusY * radiusY;
            int e = -yE / 2;
            int c = yE / radiusX;

            do {
               if (e <= 0) {
                  do {
                     this.set(oX + y, oY + x);
                     this.set(oX - y, oY + x);
                     this.set(oX + y, oY - x);
                     this.set(oX - y, oY - x);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX + y, oY + x);
                  this.set(oX - y, oY + x);
                  this.set(oX + y, oY - x);
                  this.set(oX - y, oY - x);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX, oY + x);
            this.set(oX, oY - x);
         }
      }
   }

   private void fillEllipse(int oX, int oY, int radiusX, int radiusY) {
      if (radiusX == 0) {
         this.drawLine(oX, oY - radiusY, oX, oY + radiusY);
      } else if (radiusY == 0) {
         this.drawLine(oX - radiusX, oY, oX + radiusX, oY + radiusY);
      } else {
         this.drawEllipse(oX, oY, radiusX, radiusY);
         if (radiusY <= radiusX) {
            int x = 0;
            int y = radiusY;
            int xE = 0;
            int yE = radiusX * radiusX;
            int e = -yE / 2;
            int c = yE / radiusY;

            do {
               if (e > 0) {
                  for (int y2 = oY - y; y2 <= oY + y; y2++) {
                     this.set(oX + x, y2);
                     this.set(oX - x, y2);
                  }
               } else {
                  do {
                     for (int y2 = oY - y; y2 <= oY + y; y2++) {
                        this.set(oX + x, y2);
                        this.set(oX - x, y2);
                     }

                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);
         } else {
            int x = 0;
            int y = radiusX;
            int xE = 0;
            int yE = radiusY * radiusY;
            int e = -yE / 2;
            int c = yE / radiusX;

            do {
               if (e > 0) {
                  for (int y2 = oX - y; y2 <= oX + y; y2++) {
                     this.set(y2, oY + x);
                     this.set(y2, oY - x);
                  }
               } else {
                  do {
                     for (int y2 = oX - y; y2 <= oX + y; y2++) {
                        this.set(y2, oY + x);
                        this.set(y2, oY - x);
                     }

                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);
         }
      }
   }

   private final char convertPixel(int pixel) {
      PixelPlateConfiguration configuration = PixelPlateConfiguration.INSTANCE;
      char[] rules = configuration.getRules();
      if (pixel != 0 && pixel < rules.length) {
         int ch = rules[pixel];
         return ch != 0 && ch != 32 ? (char)ch : this.convertThick(pixel);
      } else {
         return '\u0000';
      }
   }

   private final char convertDot(int pixel) {
      return DOT_TABLE[pixel];
   }

   private final char convertThick(int pixel) {
      PixelPlateConfiguration configuration = PixelPlateConfiguration.INSTANCE;
      char[] rules = configuration.getRules();
      char[] rulesThick = configuration.getRulesThick();
      if (pixel != 0 && pixel < rulesThick.length) {
         int ch = rulesThick[pixel];
         if (ch != 0) {
            return (char)ch;
         } else {
            for (int i = 0; i < 12; i++) {
               int lookup = pixel | 1 << i;
               int var8 = rulesThick[lookup];
               if (var8 != 0) {
                  return (char)var8;
               }
            }

            for (int ix = 0; ix < 12; ix++) {
               int lookup = (pixel | 1 << ix) - (1 << ix);
               int var9 = rulesThick[lookup];
               if (var9 != 0) {
                  return (char)var9;
               }
            }

            return rules[pixel];
         }
      } else {
         return '\u0000';
      }
   }

   public LocatedCharacterPlate convert() {
      char[][] result = new char[this.bounds.height][this.bounds.width];
      if (this.mode.getConverterMode() == PixelPlateConverterMode.LINE) {
         for (int y = 0; y < this.bounds.height; y++) {
            for (int x = 0; x < this.bounds.width; x++) {
               result[y][x] = this.convertPixel(this.pixels[y][x]);
            }
         }
      } else if (this.mode.getConverterMode() == PixelPlateConverterMode.DOT) {
         for (int y = 0; y < this.bounds.height; y++) {
            for (int x = 0; x < this.bounds.width; x++) {
               result[y][x] = this.convertDot(this.pixels[y][x]);
            }
         }
      } else if (this.mode.getConverterMode() == PixelPlateConverterMode.THICK) {
         for (int y = 0; y < this.bounds.height; y++) {
            for (int x = 0; x < this.bounds.width; x++) {
               char ch = this.convertThick(this.pixels[y][x]);
               if (ch != ' ' && ch != 0) {
                  if (this.feltPenChar == 'M') {
                     if (ch == '8') {
                        ch = 'M';
                     } else if (ch == 'o') {
                        ch = 'm';
                     }
                  } else if (this.feltPenChar == 'O') {
                     if (ch == '8') {
                        ch = 'O';
                     }
                  } else if (this.feltPenChar == 'X') {
                     if (ch == '8') {
                        ch = 'X';
                     }
                  } else if (this.feltPenChar == '$') {
                     if (ch == '8') {
                        ch = '$';
                     } else if (ch == 'b') {
                        ch = 'h';
                     } else if (ch == 'o') {
                        ch = 'c';
                     } else if (ch == 'P') {
                        ch = 'F';
                     } else if (ch == 'Y') {
                        ch = '?';
                     }
                  } else if (this.feltPenChar == '!') {
                     if (ch == '8') {
                        ch = '!';
                     } else if (ch == '"') {
                        ch = '\'';
                     } else if (ch == 'o') {
                        ch = '.';
                     } else if (ch == 'P') {
                        ch = '\'';
                     } else if (ch == 'p') {
                        ch = ',';
                     } else if (ch == 'd') {
                        ch = ',';
                     } else if (ch == 'F') {
                        ch = '\'';
                     } else if (ch == '_') {
                        ch = '.';
                     } else if (ch == 'L') {
                        ch = '.';
                     } else if (ch == 'b') {
                        ch = '.';
                     } else if (ch == ']') {
                        ch = ';';
                     } else if (ch == '[') {
                        ch = ';';
                     } else if (ch == 'J') {
                        ch = ',';
                     } else if (ch == '7') {
                        ch = '`';
                     } else if (ch == 'Y') {
                        ch = '`';
                     }
                  } else if (this.feltPenChar == ':') {
                     if (ch == '8') {
                        ch = ':';
                     } else if (ch == '"') {
                        ch = '\'';
                     } else if (ch == 'o') {
                        ch = '.';
                     } else if (ch == 'P') {
                        ch = '\'';
                     } else if (ch == 'p') {
                        ch = '.';
                     } else if (ch == 'd') {
                        ch = '.';
                     } else if (ch == 'F') {
                        ch = '\'';
                     } else if (ch == '_') {
                        ch = '.';
                     } else if (ch == ',') {
                        ch = '.';
                     } else if (ch == 'L') {
                        ch = '.';
                     } else if (ch == 'b') {
                        ch = '.';
                     } else if (ch == ']') {
                        ch = ':';
                     } else if (ch == '[') {
                        ch = ':';
                     } else if (ch == 'J') {
                        ch = '.';
                     } else if (ch == '7') {
                        ch = '\'';
                     } else if (ch == '`') {
                        ch = '\'';
                     } else if (ch == 'Z') {
                        ch = ':';
                     } else if (ch == 'M') {
                        ch = ':';
                     } else if (ch == '2') {
                        ch = ':';
                     } else if (ch == 'u') {
                        ch = '.';
                     } else if (ch == 'h') {
                        ch = '.';
                     } else if (ch == 'O') {
                        ch = ':';
                     } else if (ch == 'Y') {
                        ch = '\'';
                     }
                  }

                  result[y][x] = ch;
               } else {
                  result[y][x] = 0;
               }
            }
         }
      } else if (this.mode.getConverterMode() == PixelPlateConverterMode.TWO_BY_TWO) {
         for (int y = 0; y < this.bounds.height; y++) {
            for (int xx = 0; xx < this.bounds.width; xx++) {
               if (this.pixels[y][xx] != 0 && this.pixels[y][xx] < TWO_BY_TWO_CHARS.length) {
                  result[y][xx] = TWO_BY_TWO_CHARS[this.pixels[y][xx]];
               } else {
                  result[y][xx] = 0;
               }
            }
         }
      } else if (this.mode.getConverterMode() == PixelPlateConverterMode.RAW) {
         for (int y = 0; y < this.bounds.height; y++) {
            for (int xxx = 0; xxx < this.bounds.width; xxx++) {
               if (this.pixels[y][xxx] > 0) {
                  result[y][xxx] = this.pixels[y][xxx];
               } else {
                  result[y][xxx] = 0;
               }
            }
         }
      } else if (this.mode.getConverterMode() == PixelPlateConverterMode.THREE_BY_TWO) {
         for (int y = 0; y < this.bounds.height; y++) {
            for (int xxxx = 0; xxxx < this.bounds.width; xxxx++) {
               if (this.pixels[y][xxxx] != 0 && this.pixels[y][xxxx] < THREE_BY_TWO_CHARS.length) {
                  result[y][xxxx] = THREE_BY_TWO_CHARS[this.pixels[y][xxxx]];
               } else {
                  result[y][xxxx] = 0;
               }
            }
         }
      }

      LocatedCharacterPlate plate = new LocatedCharacterPlate(result, this.bounds.x, this.bounds.y);
      if (this.mode.getConverterMode() == PixelPlateConverterMode.LINE) {
         plate.replace('\u0000', ' ');
         this.filter.filter(plate, FilterMode.LINE_ART_CLEANER);
         plate.replace(' ', '\u0000');
      }

      return plate;
   }

   public void drawRectangle(Point2d p1, Point2d p2) {
      this.drawRectangle(p1.getX(), p1.getY(), p2.getX(), p2.getY());
   }

   public void drawRectangle(double p1x, double p1y, double p2x, double p2y) {
      this.drawLine(p1x, p1y, p2x, p1y);
      this.drawLine(p1x, p1y, p1x, p2y);
      this.drawLine(p2x, p1y, p2x, p2y);
      this.drawLine(p1x, p2y, p2x, p2y);
   }

   public void fillRectangle(Point2d p1, Point2d p2) {
      this.drawRectangle(p1.getX(), p1.getY(), p2.getX(), p2.getY());
      int t = this.pencilSize;
      this.pencilSize = 1;
      int x1 = this.getPhysicalX(p1.getX());
      int y1 = this.getPhysicalY(p1.getY());
      int x2 = this.getPhysicalX(p2.getX());
      int y2 = this.getPhysicalY(p2.getY());
      int dx = x1 <= x2 ? 1 : -1;
      int dy = y1 <= y2 ? 1 : -1;

      for (int x = x1; x != x2 + dx; x += dx) {
         for (int y = y1; y != y2 + dy; y += dy) {
            this.set(x, y);
         }
      }

      this.pencilSize = t;
   }

   public void drawLine(Point2d p1, Point2d p2) {
      this.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
   }

   public void drawLine(double x1, double y1, double x2, double y2) {
      int xx1 = this.getPhysicalX(x1);
      int yy1 = this.getPhysicalY(y1);
      int xx2 = this.getPhysicalX(x2);
      int yy2 = this.getPhysicalY(y2);
      this.drawLineBresenham(xx1, yy1, xx2, yy2);
   }

   @Override
   public void set(int x, int y, char ch) {
      this.setCharacter(ch);
      this.set(x, y);
   }

   public void set(int x, int y) {
      this.set(x, y, true);
   }

   public void set(int x, int y, boolean what) {
      if (this.pencilSize == 1) {
         this.setInternal(x, y, what);
      } else {
         this.setInternal(x, y);
         this.setInternal(x - 1, y, what);
         this.setInternal(x + 1, y, what);
         this.setInternal(x, y - 1, what);
         this.setInternal(x, y + 1, what);
         this.setInternal(x - 1, y - 1, what);
         this.setInternal(x - 1, y + 1, what);
         this.setInternal(x + 1, y - 1, what);
         this.setInternal(x + 1, y + 1, what);
         this.setInternal(x + 2, y, what);
         this.setInternal(x - 2, y, what);
         if (this.pencilSize != 2) {
            this.setInternal(x + 3, y, what);
            this.setInternal(x + 2, y - 1, what);
            this.setInternal(x + 1, y - 2, what);
            this.setInternal(x, y - 2, what);
            this.setInternal(x - 1, y - 2, what);
            this.setInternal(x - 2, y - 1, what);
            this.setInternal(x - 3, y, what);
            this.setInternal(x - 2, y + 1, what);
            this.setInternal(x - 1, y + 2, what);
            this.setInternal(x, y + 2, what);
            this.setInternal(x + 1, y + 2, what);
            this.setInternal(x + 2, y + 1, what);
            if (this.pencilSize != 3) {
               this.setInternal(x + 4, y, what);
               this.setInternal(x + 4, y + 1, what);
               this.setInternal(x + 4, y - 1, what);
               this.setInternal(x + 3, y + 1, what);
               this.setInternal(x + 3, y - 1, what);
               this.setInternal(x + 3, y - 2, what);
               this.setInternal(x + 2, y - 2, what);
               this.setInternal(x + 3, y + 2, what);
               this.setInternal(x + 2, y + 2, what);
               this.setInternal(x + 1, y - 3, what);
               this.setInternal(x + 1, y + 3, what);
               this.setInternal(x, y - 3, what);
               this.setInternal(x, y + 3, what);
               this.setInternal(x - 4, y, what);
               this.setInternal(x - 4, y + 1, what);
               this.setInternal(x - 4, y - 1, what);
               this.setInternal(x - 3, y + 1, what);
               this.setInternal(x - 3, y - 1, what);
               this.setInternal(x - 3, y - 2, what);
               this.setInternal(x - 2, y - 2, what);
               this.setInternal(x - 3, y + 2, what);
               this.setInternal(x - 2, y + 2, what);
               this.setInternal(x - 1, y - 3, what);
               this.setInternal(x - 1, y + 3, what);
            }
         }
      }
   }

   public boolean isSet(int x, int y) {
      int characterXIndex;
      if (x < 0) {
         characterXIndex = (x - this.getRasterX() + 1) / this.getRasterX();
      } else {
         characterXIndex = x / this.getRasterX();
      }

      int insideCharacterXIndex = x - characterXIndex * this.getRasterX();
      characterXIndex -= this.bounds.x;
      if (characterXIndex >= this.bounds.x && characterXIndex < this.bounds.width) {
         int characterYIndex;
         if (y < 0) {
            characterYIndex = (y - this.getRasterY() + 1) / this.getRasterY();
         } else {
            characterYIndex = y / this.getRasterY();
         }

         int insideCharacterYIndex = y - characterYIndex * this.getRasterY();
         characterYIndex -= this.bounds.y;
         if (characterYIndex >= this.bounds.y && characterYIndex < this.bounds.height) {
            return this.mode.getConverterMode() == PixelPlateConverterMode.RAW
               ? this.pixels[characterYIndex][characterXIndex] == this.character
               : this.pixels[characterYIndex][characterXIndex]
                  == (this.pixels[characterYIndex][characterXIndex] | 1 << insideCharacterYIndex + insideCharacterXIndex * this.getRasterY());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void setInternal(int x, int y) {
      this.setInternal(x, y, true);
   }

   private synchronized void setInternal(int x, int y, boolean set) {
      int characterXIndex;
      if (x < 0) {
         characterXIndex = (x - this.getRasterX() + 1) / this.getRasterX();
      } else {
         characterXIndex = x / this.getRasterX();
      }

      int insideCharacterXIndex = x - characterXIndex * this.getRasterX();
      if (characterXIndex >= this.maximumBounds.x && characterXIndex < this.maximumBounds.x + this.maximumBounds.width) {
         characterXIndex -= this.bounds.x;
         int characterYIndex;
         if (y < 0) {
            characterYIndex = (y - this.getRasterY() + 1) / this.getRasterY();
         } else {
            characterYIndex = y / this.getRasterY();
         }

         int insideCharacterYIndex = y - characterYIndex * this.getRasterY();
         if (characterYIndex >= this.maximumBounds.y && characterYIndex < this.maximumBounds.y + this.maximumBounds.height) {
            characterYIndex -= this.bounds.y;
            if (characterXIndex < 0) {
               int additionalColumnsCount = 2 - characterXIndex;
               this.addColumnsWest(additionalColumnsCount);
               characterXIndex = 2;
            }

            if (characterXIndex >= this.bounds.width) {
               int additionalColumnsCount = characterXIndex - this.bounds.width + 1 + 2;
               this.addColumnsEast(additionalColumnsCount);
            }

            if (characterYIndex < 0) {
               int additionalRowsCount = 2 - characterYIndex;
               this.addRowsNorth(additionalRowsCount);
               characterYIndex = 2;
            }

            if (characterYIndex >= this.bounds.height) {
               int additionalRowsCount = characterYIndex - this.bounds.height + 1 + 2;
               this.addRowsSouth(additionalRowsCount);
            }

            if (set) {
               if (this.mode.getConverterMode() == PixelPlateConverterMode.RAW) {
                  this.pixels[characterYIndex][characterXIndex] = this.character;
               } else {
                  this.pixels[characterYIndex][characterXIndex] = (char)(
                     this.pixels[characterYIndex][characterXIndex] | 1 << insideCharacterYIndex + insideCharacterXIndex * this.getRasterY()
                  );
               }
            } else if (this.mode.getConverterMode() == PixelPlateConverterMode.RAW) {
               this.pixels[characterYIndex][characterXIndex] = 0;
            } else {
               this.pixels[characterYIndex][characterXIndex] = (char)(
                  this.pixels[characterYIndex][characterXIndex]
                     | 1
                        << insideCharacterYIndex + insideCharacterXIndex * this.getRasterY() - 1
                        << insideCharacterYIndex + insideCharacterXIndex * this.getRasterY()
               );
            }
         }
      }
   }

   private void addRowsSouth(int additionalRowsCount) {
      char[][] newChars = new char[this.bounds.height + additionalRowsCount][this.bounds.width];

       if (this.bounds.height >= 0) System.arraycopy(this.pixels, 0, newChars, 0, this.bounds.height);

      this.bounds = new Rectangle(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height + additionalRowsCount);
      this.pixels = newChars;
   }

   private void addRowsNorth(int additionalRowsCount) {
      char[][] newChars = new char[this.bounds.height + additionalRowsCount][this.bounds.width];

       if (this.bounds.height >= 0)
           System.arraycopy(this.pixels, 0, newChars, 0 + additionalRowsCount, this.bounds.height);

      this.bounds = new Rectangle(this.bounds.x, this.bounds.y - additionalRowsCount, this.bounds.width, this.bounds.height + additionalRowsCount);
      this.pixels = newChars;
   }

   private void addColumnsEast(int additionalColumnsCount) {
      char[][] newChars = new char[this.bounds.height][this.bounds.width + additionalColumnsCount];

      for (int j = 0; j < this.bounds.height; j++) {
         System.arraycopy(this.pixels[j], 0, newChars[j], 0, this.bounds.width);
      }

      this.bounds = new Rectangle(this.bounds.x, this.bounds.y, this.bounds.width + additionalColumnsCount, this.bounds.height);
      this.pixels = newChars;
   }

   private void addColumnsWest(int additionalColumnsCount) {
      char[][] newChars = new char[this.bounds.height][this.bounds.width + additionalColumnsCount];

      for (int j = 0; j < this.bounds.height; j++) {
         System.arraycopy(this.pixels[j], 0, newChars[j], additionalColumnsCount, this.bounds.width);
      }

      this.bounds = new Rectangle(this.bounds.x - additionalColumnsCount, this.bounds.y, this.bounds.width + additionalColumnsCount, this.bounds.height);
      this.pixels = newChars;
   }

   public void drawLineBresenham(int x1, int y1, int x2, int y2) {
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
            this.set(x, y);
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
            this.set(x, y);
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

   private int getRasterX() {
      return this.mode.getRasterX();
   }

   private int getRasterY() {
      return this.mode.getRasterY();
   }

   public int getPixels(int x, int y) {
      return this.pixels[y][x];
   }
}
